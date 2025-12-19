package store.babel.babel.domain.post.controller.claude;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ClaudeSessionManager
{
    private Map<Long, Optional<ClaudeSession>> claudeSessionMap;

    @PostConstruct
    public void init()
    {
        claudeSessionMap = new ConcurrentHashMap<>();
    }

    public void createSession(Long userId)
    {
        if (hasSession(userId))
        {
            log.warn("이미 세션이 존재함: userId={}", userId);
            removeSession(userId);
        }

        log.info("세션 생성: userId={}", userId);
        ClaudeSession session = ClaudeSession.create();

        session.setOnCompletion(() -> removeSession(userId));
        session.setOnTimeout(() -> removeSession(userId));
        session.setOnError((e) -> removeSession(userId));

        claudeSessionMap.put(userId, Optional.of(session));
    }

    public ClaudeSession getSession(Long userId)
    {
        return claudeSessionMap.get(userId).orElseThrow();
    }

    public boolean hasSession(Long userId)
    {
        return claudeSessionMap.containsKey(userId)
                && claudeSessionMap.get(userId).isPresent();
    }

    public void removeSession(Long userId)
    {
        log.info("세션 제거: userId={}", userId);
        claudeSessionMap.remove(userId);
    }
}
