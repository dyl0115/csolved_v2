package store.babel.babel.global.llm.claude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClaudeChatTurn<REQ, RES>
{
    private REQ userMessage;
    private RES assistantMessage;

    public static <REQ, RES> ClaudeChatTurn<REQ, RES> open(REQ request)
    {
        return ClaudeChatTurn.<REQ, RES>builder()
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
