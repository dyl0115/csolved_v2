package store.babel.babel.global.llm.claude;

import com.anthropic.models.beta.messages.MessageCreateParams;
import com.anthropic.models.beta.messages.StructuredMessageCreateParams;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.domain.assistant.dto.PostAssistResponse;
import store.babel.babel.global.llm.PromptManager;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ClaudeMessageBuilder
{
    private static final String SYSTEM_PROMPT = "prompts/post-create-system.md";
    private static final String USER_PROMPT = "prompts/post-create-user.md";

    private final PromptManager promptManager;
    private final ObjectMapper objectMapper;

    public StructuredMessageCreateParams<PostAssistResponse> build(
            List<ClaudeChatTurn<PostAssistRequest, PostAssistResponse>> history)
    {
        StructuredMessageCreateParams.Builder<PostAssistResponse> builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5_20251001)
                .maxTokens(2048L)
                .system(promptManager.load(SYSTEM_PROMPT))
                .outputFormat(PostAssistResponse.class);

        history.forEach(
                turn ->
                {
                    builder.addUserMessage(promptManager.loadAndRender(USER_PROMPT, turn.getUserMessage()));
                    if (turn.getAssistantMessage() != null)
                    {
                        builder.addAssistantMessage(toJson(turn.getAssistantMessage()));
                    }
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
