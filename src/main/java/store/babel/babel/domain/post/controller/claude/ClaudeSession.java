package store.babel.babel.domain.post.controller.claude;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Builder
public class ClaudeSession
{
    private SseEmitter emitter;
    private List<ClaudeMessage> history;

    public static ClaudeSession create()
    {
        return ClaudeSession.builder()
                .emitter(new SseEmitter(60000L))
                .history(new CopyOnWriteArrayList<>())
                .build();
    }

    public void addHistory(ClaudeMessage message)
    {
        history.add(message);
    }
}
