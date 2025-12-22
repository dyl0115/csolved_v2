package store.babel.babel.domain.post.controller.claude;

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
    private ChatHistory<PostAssistRequest, PostAssistResponse> chatHistory;

    public static AssistantChatSession create()
    {
        SseEmitter emitter = new SseEmitter(0L);

        return AssistantChatSession.builder()
                .chatHistory(new ChatHistory<>())
                .chatEmitter(emitter)
                .build();
    }

    public void openChatTurn(PostAssistRequest request)
    {
        chatHistory.openTurn(request);
    }

    public void closeChatTurn(PostAssistResponse response)
    {
        chatHistory.closeTurn(response);
    }

    public void send(String text)
    {
        try
        {
            log.debug("SSE 전송: {}", text);
            chatEmitter.send(SseEmitter.event()
                    .name("message")
                    .data(text));
        }
        catch (IOException e)
        {
            throw new RuntimeException("SSE 전송 실패", e);
        }
    }

    public void onCompletion(Runnable callback)
    {
        chatEmitter.onCompletion(() ->
        {
            log.info("SSE 연결완료");
            chatHistory.clearAll();
            if (callback != null) callback.run();
        });
    }

    public void onTimeout(Runnable callback)
    {
        chatEmitter.onTimeout(() ->
        {
            log.info("SSE 타임아웃");
            chatEmitter.complete();
            chatHistory.clearAll();
            if (callback != null) callback.run();
        });
    }

    public void onError(Runnable callback)
    {
        chatEmitter.onError((e) ->
        {
            log.error("SSE 오류: {}", e.getMessage());
            chatEmitter.completeWithError(e);
            chatHistory.clearAll();
            if (callback != null) callback.run();
        });
    }
}
