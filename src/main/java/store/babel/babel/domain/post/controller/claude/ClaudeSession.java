package store.babel.babel.domain.post.controller.claude;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter
@Builder
public class ClaudeSession
{
    private SseEmitter emitter;
    private List<ClaudeMessage> history;

    public static ClaudeSession create()
    {
        SseEmitter emitter = new SseEmitter(0L);

        emitter.onCompletion(() ->
        {
            log.info("SSE completed");
        });

        emitter.onTimeout(() ->
        {
            log.info("SSE timeout");
            emitter.complete();
        });

        emitter.onError((e) ->
        {
            log.error("SSE error: {}", e.getMessage());
            emitter.completeWithError(e);
        });

        return ClaudeSession.builder()
                .emitter(emitter)
                .history(new CopyOnWriteArrayList<>())
                .build();
    }

    public void addHistory(ClaudeMessage message)
    {
        history.add(message);
    }
}
