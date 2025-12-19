package store.babel.babel.domain.post.controller.claude;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
        claudeSessionMap.put(userId, Optional.of(ClaudeSession.create()));
    }

    public ClaudeSession getSession(Long userId)
    {
        return claudeSessionMap.get(userId).orElseThrow();
    }
}
