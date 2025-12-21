package store.babel.babel.domain.post.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.helpers.BetaMessageAccumulator;
import com.anthropic.models.beta.messages.*;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.post.controller.claude.ClaudeMessage;
import store.babel.babel.domain.post.controller.claude.ClaudeSession;
import store.babel.babel.domain.post.controller.claude.ClaudeSessionManager;
import store.babel.babel.global.utils.PromptManager;

import java.io.IOException;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class ClaudePostService
{
    private static final String POST_CREATE_ASSISTANT_PROMPT = "prompts/post-create-assistant.md";

    private final ClaudeSessionManager claudeSessionManager;
    private final AnthropicClient claudeClient;
    private final PromptManager promptManager;
    private final ObjectMapper objectMapper;

    public SseEmitter connect(Long userId)
    {
        claudeSessionManager.createSession(userId);
        return claudeSessionManager.getSession(userId).getEmitter();
    }

    @Async
    public void assistPost(Long userId, ClaudeMessage message)
    {
        ClaudeSession session = claudeSessionManager.getSession(userId);
        session.addHistory(message);

        BetaMessageAccumulator accumulator = streamResponse(session);
        ClaudeMessage response = parseResponse(accumulator);
        session.addHistory(response);
    }

    private BetaMessageAccumulator streamResponse(ClaudeSession session)
    {
        SseEmitter emitter = session.getEmitter();
        List<ClaudeMessage> history = session.getHistory();
        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();

        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse
                     = claudeClient.beta().messages().createStreaming(buildRequestParams(history)))
        {
            streamResponse.stream()
                    .peek(accumulator::accumulate)
                    .forEach(event -> handleStreamEvent(emitter, event));
        }

        return accumulator;
    }

    private void handleStreamEvent(SseEmitter emitter, BetaRawMessageStreamEvent event)
    {
        if (event.contentBlockDelta().isEmpty()) return;

        event.contentBlockDelta().stream()
                .flatMap(contentBlock -> contentBlock.delta().text().stream())
                .map(BetaTextDelta::text)
                .forEach(text -> sendToClient(emitter, text));
    }

    private void sendToClient(SseEmitter emitter, String text)
    {
        try
        {
            log.info("text: {}", text);
            emitter.send(SseEmitter.event()
                    .name("message")
                    .data(text));
        }
        catch (IOException e)
        {
            throw new RuntimeException("SSE 전송 실패", e);
        }
    }

    private ClaudeMessage parseResponse(BetaMessageAccumulator accumulator)
    {
        String jsonText = accumulator.message(ClaudeMessage.class)
                .content()
                .stream()
                .flatMap(block -> block.rawContentBlock().text().stream())
                .map(BetaTextBlock::text)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("응답 텍스트 없음"));
        try
        {
            return objectMapper.readValue(jsonText, ClaudeMessage.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("응답 파싱 실패", e);
        }
    }

    private StructuredMessageCreateParams<ClaudeMessage> buildRequestParams(List<ClaudeMessage> history)
    {
        StructuredMessageCreateParams.Builder<ClaudeMessage> builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5_20251001)
                .maxTokens(2048L)
                .outputFormat(ClaudeMessage.class);

        history.forEach(message ->
        {
            if (Objects.equals(message.getRole(), "assistant"))
            {
                builder.addAssistantMessage(buildPrompt(message));
            }
            else if (Objects.equals(message.getRole(), "user"))
            {
                builder.addUserMessage(buildPrompt(message));
            }
        });

        return builder.build();
    }

    private String buildPrompt(ClaudeMessage message)
    {
        return promptManager.loadAndRender(POST_CREATE_ASSISTANT_PROMPT, message);
    }
}
