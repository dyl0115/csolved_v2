package store.babel.babel.domain.post.controller.claude;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ChatHistory<REQ, RES>
{
    private final List<ChatTurn<REQ, RES>> turns;

    public ChatHistory()
    {
        turns = new CopyOnWriteArrayList<>();
    }

    public void openTurn(REQ request)
    {
        ChatTurn<REQ, RES> turn = ChatTurn.open(request);
        turns.add(turn);
    }

    public void closeTurn(RES response)
    {
        if (turns.isEmpty()) return;
        turns.get(turns.size() - 1).close(response);
    }

    public void forEach(Consumer<REQ> onUser, Consumer<RES> onAssistant)
    {
        turns.forEach(turn ->
        {
            onUser.accept(turn.getUserMessage());
            if (turn.isClosed()) onAssistant.accept(turn.getAssistantMessage());
        });
    }

    public void clearAll()
    {
        turns.clear();
    }
}
