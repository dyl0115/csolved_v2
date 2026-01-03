package store.babel.babel.global.llm.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.errors.AnthropicException;
import com.anthropic.errors.AnthropicServiceException;
import com.anthropic.helpers.BetaMessageAccumulator;
import com.anthropic.models.beta.messages.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.domain.assistant.dto.PostAssistResponse;
import store.babel.babel.domain.assistant.session.AssistantChatSession;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.llm.LlmClient;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClaudeLlmClient implements LlmClient<PostAssistRequest>
{
    private final ClaudeChatHistoryManager<PostAssistRequest, PostAssistResponse> historyManager;
    private final ClaudeMessageBuilder messageBuilder;
    private final AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper;

    @Override
    public void openSession(Long userId)
    {
        historyManager.createHistory(userId);
    }

    @Override
    public void stream(AssistantChatSession session, PostAssistRequest request)
    {
        Long userId = request.getAuthorId();

        historyManager.openTurn(userId, request);

        StructuredMessageCreateParams<PostAssistResponse> message
                = messageBuilder.build(historyManager.getHistory(userId));

        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();

        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse =
                     anthropicClient.beta().messages().createStreaming(message))
        {
            streamResponse.stream()
                    .peek(accumulator::accumulate)
                    .forEach(event -> handleTextDelta(event, session::send));
        }
        catch (AnthropicServiceException exception)
        {
//            if (exception.statusCode() == 429)
//            {
//                throw new BabelException(ExceptionCode.AI_RESOURCE_EXHAUSTED);
//            }

            log.error("앤트로픽 서버 예외 발생" + exception.statusCode() + " "
                    + exception.getMessage());
        }

        historyManager.closeTurn(userId, parseResponse(accumulator));
    }

    @Override
    public void closeSession(Long userId)
    {
        historyManager.clearAll(userId);
    }

    private void handleTextDelta(BetaRawMessageStreamEvent event, Consumer<String> onText)
    {
        if (event.contentBlockDelta().isEmpty()) return;

        event.contentBlockDelta().stream()
                .flatMap(contentBlock -> contentBlock.delta().text().stream())
                .map(BetaTextDelta::text)
                .forEach(onText);
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
}
