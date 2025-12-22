package store.babel.babel.domain.post.controller.claude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatTurn<REQ, RES>
{
    private REQ userMessage;
    private RES assistantMessage;

    public static <REQ, RES> ChatTurn<REQ, RES> open(REQ request)
    {
        return ChatTurn.<REQ, RES>builder()
                .userMessage(request)
                .build();
    }

    public void close(RES response)
    {
        this.assistantMessage = response;
    }

    public boolean isClosed()
    {
        return this.assistantMessage != null;
    }
}
