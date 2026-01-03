package store.babel.babel.domain.assistant.session;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Getter
@Builder
public class AssistantChatSession
{
    private SseEmitter chatEmitter;

    public static AssistantChatSession create()
    {
        SseEmitter emitter = new SseEmitter(0L);

        return AssistantChatSession.builder()
                .chatEmitter(emitter)
                .build();
    }

    public void send(String text)
    {
        try
        {
            log.info("SSE 전송: {}", text);
            chatEmitter.send(SseEmitter.event()
                    .name("message")
                    .data(text));
        }
        catch (IOException e)
        {
            throw new RuntimeException("SSE 전송 실패", e);
        }
    }

    public AssistantChatSession onInitialize(Runnable onInitialize)
    {
        onInitialize.run();
        return this;
    }

    public AssistantChatSession onCompletion(Runnable onCompletion)
    {
        chatEmitter.onCompletion(() ->
        {
            log.info("SSE 연결완료");
            if (onCompletion != null) onCompletion.run();
        });

        return this;
    }

    public AssistantChatSession onTimeout(Runnable onTimeout)
    {
        chatEmitter.onTimeout(() ->
        {
            log.info("SSE 타임아웃");
            chatEmitter.complete();
            if (onTimeout != null) onTimeout.run();
        });

        return this;
    }

    public AssistantChatSession onError(Runnable onError)
    {
        chatEmitter.onError((e) ->
        {
            log.error("SSE 오류: {}", e.getMessage());
            chatEmitter.completeWithError(e);
            if (onError != null) onError.run();
        });

        return this;
    }
}
