package store.csolved.csolved.domain.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.csolved.csolved.domain.assistant.dto.PostAssistRequest;
import store.csolved.csolved.domain.assistant.session.AssistantChatSession;
import store.csolved.csolved.domain.assistant.session.AssistantChatSessionManager;
import store.csolved.csolved.global.llm.LlmClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostAssistService
{
    private final AssistantChatSessionManager chatSessionManager;
    private final LlmClient<PostAssistRequest> llmClient;

    public SseEmitter connect(Long userId)
    {
        chatSessionManager.createSession(userId)
                .onInitialize(() ->
                {
                    llmClient.openSession(userId);
                })
                .onCompletion(() ->
                {
                    llmClient.closeSession(userId);
                    chatSessionManager.removeSession(userId);
                })
                .onTimeout(() ->
                {
                    llmClient.closeSession(userId);
                    chatSessionManager.removeSession(userId);
                })
                .onError(() ->
                {
                    llmClient.closeSession(userId);
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
