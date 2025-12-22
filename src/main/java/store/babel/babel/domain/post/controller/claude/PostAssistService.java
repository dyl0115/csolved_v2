package store.babel.babel.domain.post.controller.claude;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostAssistService
{
    private final AssistantChatSessionManager sessionManager;
    private final LlmClient<PostAssistResponse> llmClient;

    public SseEmitter connect(Long userId)
    {
        sessionManager.createSession(userId);
        return sessionManager.getSession(userId).getChatEmitter();
    }

    @Async
    public void assistPost(AssistantChatSession session, PostAssistRequest request)
    {
        session.openChatTurn(request);

        PostAssistResponse response = llmClient.stream(session.getChatHistory(), session::send);

        session.closeChatTurn(response);
    }
}
