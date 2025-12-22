package store.babel.babel.domain.post.controller.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.helpers.BetaMessageAccumulator;
import com.anthropic.models.beta.messages.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClaudeLlmClient implements LlmClient<PostAssistResponse>
{
    private final AnthropicClient anthropicClient;
    private final ClaudeMessageBuilder messageBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public PostAssistResponse stream(ChatHistory<PostAssistRequest, PostAssistResponse> history,
                                     Consumer<String> onText)
    {
        StructuredMessageCreateParams<PostAssistResponse> request = messageBuilder.build(history);
        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();

        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse =
                     anthropicClient.beta().messages().createStreaming(request))
        {
            streamResponse.stream()
                    .peek(accumulator::accumulate)
                    .forEach(event -> handleStreamEvent(event, onText));
        }

        return parseResponse(accumulator);
    }

    private void handleStreamEvent(BetaRawMessageStreamEvent event, Consumer<String> onText)
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
