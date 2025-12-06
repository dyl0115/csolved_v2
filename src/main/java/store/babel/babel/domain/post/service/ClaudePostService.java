package store.babel.babel.domain.post.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.MessageParam;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.RawMessageStreamEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.post.controller.claude.ClaudeMessage;
import store.babel.babel.domain.post.controller.claude.ClaudeSession;
import store.babel.babel.domain.post.controller.claude.ClaudeSessionManager;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClaudePostService
{
    private final ClaudeSessionManager claudeSessionManager;
    private final AnthropicClient claudeClient;

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

        MessageCreateParams params = createParams(history);


        try (StreamResponse<RawMessageStreamEvent> streamResponse
                     = claudeClient.messages().createStreaming(params))
        {
            streamResponse.stream().forEach(System.out::println);
            System.out.println("No more chunks!");
        }

    }

    private MessageCreateParams createParams(List<ClaudeMessage> history)
    {
        MessageCreateParams.Builder builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_SONNET_4_5_20250929)
                .maxTokens(2048L);

        history.forEach(message ->
        {
            if (message.getRole() == MessageParam.Role.ASSISTANT)
            {
                builder.addAssistantMessage(createStringMessage(message));
            }
            else if (message.getRole() == MessageParam.Role.USER)
            {
                builder.addUserMessage(createStringMessage(message));
            }
        });

        return builder.build();
    }

    private String createStringMessage(ClaudeMessage message)
    {
        return """
                역할: %s
                제목: %s
                내용: %s
                태그: %s
                메시지: %s
                """.formatted(message.getRole(),
                message.getTitle(),
                message.getContent(),
                message.getTags().toString(),
                message.getMessage());
    }
}
