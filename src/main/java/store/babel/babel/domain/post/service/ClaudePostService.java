package store.babel.babel.domain.post.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.helpers.BetaMessageAccumulator;
import com.anthropic.models.beta.messages.*;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private static final String POST_CREATE_ASSISTANT_PROMPT = "/prompts/post-create-assistant.md";

    private final ClaudeSessionManager claudeSessionManager;
    private final AnthropicClient claudeClient;
    private final ObjectMapper objectMapper;
    private final PromptManager promptManager;

    public SseEmitter connect(Long userId)
    {
        claudeSessionManager.createSession(userId);
        return claudeSessionManager.getSession(userId).getEmitter();
    }
    
    @Async
    public void stream(Long userId, ClaudeMessage message)
    {
        ClaudeSession session = claudeSessionManager.getSession(userId);
        SseEmitter emitter = session.getEmitter();

        session.addHistory(message);

        List<ClaudeMessage> history = session.getHistory();
        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();

        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse
                     = claudeClient.beta().messages().createStreaming(createParams(history)))
        {
            streamResponse.stream()
                    .peek(accumulator::accumulate)
                    .forEach(
                            event ->
                            {
                                if (event.contentBlockDelta().isPresent())
                                {
                                    event.contentBlockDelta().stream()
                                            .flatMap(contentBlock -> contentBlock.delta().text().stream())
                                            .map(BetaTextDelta::text)
                                            .forEach(text ->
                                            {
                                                try
                                                {
                                                    log.info("text: " + text);
                                                    emitter.send(SseEmitter.event()
                                                            .name("message")
                                                            .data(text));
                                                }
                                                catch (IOException e)
                                                {
                                                    throw new RuntimeException(e);
                                                }
                                            });
                                }
                            }
                    );
        }

        String jsonText = accumulator.message(ClaudeMessage.class)
                .content()
                .stream()
                .flatMap(block -> block.rawContentBlock().text().stream())
                .map(BetaTextBlock::text)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Text Content"));

        try
        {
            ClaudeMessage claudeMessage = objectMapper.readValue(jsonText, ClaudeMessage.class);
            session.addHistory(claudeMessage);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private StructuredMessageCreateParams<ClaudeMessage> createParams(List<ClaudeMessage> history)
    {
        StructuredMessageCreateParams.Builder<ClaudeMessage> builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5_20251001)
                .maxTokens(2048L)
                .outputFormat(ClaudeMessage.class);

        history.forEach(message ->
        {
            if (Objects.equals(message.getRole(), "assistant"))
            {
                builder.addAssistantMessage(createStringMessage(message));
            }
            else if (Objects.equals(message.getRole(), "user"))
            {
                builder.addUserMessage(createStringMessage(message));
            }
        });

        return builder.build();
    }


    private String createStringMessage(ClaudeMessage message)
    {
        return promptManager.loadAndRender(POST_CREATE_ASSISTANT_PROMPT, message);
    }
}
