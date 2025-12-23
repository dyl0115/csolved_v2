package store.babel.babel.domain.assistant.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.llm.LlmClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AssistantChatSessionManager
{
    private final Map<Long, AssistantChatSession> sessionMap = new ConcurrentHashMap<>();

    public AssistantChatSession createSession(Long userId)
    {
        if (hasSession(userId))
        {
            log.warn("이미 세션이 존재함: userId={}", userId);
            removeSession(userId);
        }

        log.info("세션 생성: userId={}", userId);
        AssistantChatSession session = AssistantChatSession.create();

        sessionMap.put(userId, session);

        return sessionMap.get(userId);
    }

    public AssistantChatSession getSession(Long userId)
    {
        AssistantChatSession session = sessionMap.get(userId);
        if (!hasSession(userId))
        {
            throw new BabelException(ExceptionCode.CHAT_SESSION_NOT_FOUND);
        }
        return session;
    }

    public boolean hasSession(Long userId)
    {
        return sessionMap.containsKey(userId);
    }

    public void removeSession(Long userId)
    {
        log.info("세션 제거: userId={}", userId);
        sessionMap.remove(userId);
    }
}
