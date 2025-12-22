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
import store.babel.babel.domain.post.controller.claude.*;
import store.babel.babel.global.utils.PromptManager;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@Service
public class ClaudePostService
{
    private static final String POST_CREATE_SYSTEM_PROMPT = "prompts/post-create-system.md";
    private static final String POST_CREATE_USER_PROMPT = "prompts/post-create-user.md";

    private final AssistantChatSessionManager claudeSessionManager;
    private final AnthropicClient claudeClient;
    private final PromptManager promptManager;
    private final ObjectMapper objectMapper;

    public SseEmitter connect(Long userId)
    {
        claudeSessionManager.createSession(userId);
        return claudeSessionManager.getSession(userId).getChatEmitter();
    }

    @Async
    public void assistPost(Long userId, PostAssistRequest request)
    {
        AssistantChatSession session = claudeSessionManager.getSession(userId);
        session.openChatTurn(request);

        BetaMessageAccumulator accumulator = streamResponse(session);
        PostAssistResponse response = parseResponse(accumulator);
        session.closeChatTurn(response);
    }

    private BetaMessageAccumulator streamResponse(AssistantChatSession session)
    {
        SseEmitter emitter = session.getChatEmitter();
        ChatHistory<PostAssistRequest, PostAssistResponse> history = session.getChatHistory();

        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();

        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse =
                     claudeClient.beta()
                             .messages()
                             .createStreaming(buildRequestParams(history)))
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
                .flatMap(contentBlock -> contentBlock
                        .delta()
                        .text()
                        .stream())
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

    private PostAssistResponse parseResponse(BetaMessageAccumulator accumulator)
    {
        String jsonText = accumulator.message(PostAssistResponse.class)
                .content()
                .stream()
                .flatMap(block -> block.rawContentBlock().text().stream())
                .map(BetaTextBlock::text)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("응답 텍스트 없음"));
        try
        {
            return objectMapper.readValue(jsonText, PostAssistResponse.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("응답 파싱 실패", e);
        }
    }

    private StructuredMessageCreateParams<PostAssistResponse> buildRequestParams(ChatHistory<PostAssistRequest, PostAssistResponse> history)
    {
        StructuredMessageCreateParams.Builder<PostAssistResponse> builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5_20251001)
                .maxTokens(2048L)
                .system(promptManager.load(POST_CREATE_SYSTEM_PROMPT))
                .outputFormat(PostAssistResponse.class);

        history.forEach((request, response) ->
        {
            builder.addUserMessage(promptManager.loadAndRender(POST_CREATE_USER_PROMPT, request));
            if (response != null) builder.addAssistantMessage(toJson(response));
        });

        return builder.build();
    }

    private String toJson(PostAssistResponse response)
    {
        try
        {
            return objectMapper.writeValueAsString(response);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("메시지 JSON 변환 실패", e);
        }
    }
}
