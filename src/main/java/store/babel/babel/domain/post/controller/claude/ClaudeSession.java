package store.babel.babel.domain.post.controller.claude;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

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

        return ClaudeSession.builder()
                .emitter(emitter)
                .history(new CopyOnWriteArrayList<>())
                .build();
    }

    public void addHistory(ClaudeMessage message)
    {
        history.add(message);
    }

    public void setOnCompletion(Runnable callback)
    {
        emitter.onCompletion(() ->
        {
            log.info("SSE 연결완료");
            history.clear();
            if (callback != null) callback.run();
        });
    }

    public void setOnTimeout(Runnable callback)
    {
        emitter.onTimeout(() ->
        {
            log.info("SSE 타임아웃");
            emitter.complete();
            history.clear();
            if (callback != null) callback.run();
        });
    }

    public void setOnError(Consumer<Throwable> callback)
    {
        emitter.onError((e) ->
        {
            log.error("SSE 오류: {}", e.getMessage());
            emitter.completeWithError(e);
            history.clear();
            if (callback != null) callback.accept(e);
        });
    }
}
