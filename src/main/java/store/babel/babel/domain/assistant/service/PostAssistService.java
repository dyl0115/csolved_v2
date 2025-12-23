package store.babel.babel.domain.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.domain.assistant.session.AssistantChatSession;
import store.babel.babel.domain.assistant.session.AssistantChatSessionManager;
import store.babel.babel.global.llm.LlmClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostAssistService
{
    private final AssistantChatSessionManager chatSessionManager;
    private final LlmClient<PostAssistRequest> llmClient;

    public SseEmitter connect(Long userId)
    {
        //여기서 그 LLM Client의 초기화가 이루어져야 함.

        chatSessionManager.createSession(userId)
                .onCompletion(() ->
                {
                    llmClient.cleanUp(userId);
                    chatSessionManager.removeSession(userId);
                })
                .onTimeout(() ->
                {
                    llmClient.cleanUp(userId);
                    chatSessionManager.removeSession(userId);
                })
                .onError(() ->
                {
                    llmClient.cleanUp(userId);
                    chatSessionManager.removeSession(userId);
                });

        return chatSessionManager.getSession(userId).getChatEmitter();
    }

    @Async
    public void assistPost(AssistantChatSession chatSession, PostAssistRequest request)
    {
        llmClient.stream(chatSession, request);
    }
}
