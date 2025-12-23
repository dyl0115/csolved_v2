package store.babel.babel.global.llm.claude;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class ClaudeChatHistoryManager<REQ, RES>
{
    private Map<Long, List<ClaudeChatTurn<REQ, RES>>> historyMap;

    @PostConstruct
    public void init()
    {
        historyMap = new ConcurrentHashMap<>();
    }

    public void createHistory(Long userId)
    {
        historyMap.put(userId, new CopyOnWriteArrayList<>());
    }

    public boolean hasHistory(Long userId)
    {
        return historyMap.get(userId) != null;
    }

    public List<ClaudeChatTurn<REQ, RES>> getHistory(Long userId)
    {
        if (!hasHistory(userId)) createHistory(userId);
        return historyMap.get(userId);
    }

    public void openTurn(Long userId, REQ request)
    {
        List<ClaudeChatTurn<REQ, RES>> turns = getHistory(userId);
        ClaudeChatTurn<REQ, RES> turn = ClaudeChatTurn.open(request);
        turns.add(turn);
    }

    public void closeTurn(Long userId, RES response)
    {
        List<ClaudeChatTurn<REQ, RES>> turns = historyMap.get(userId);
        if (turns.isEmpty()) return;
        turns.get(turns.size() - 1).close(response);
    }

    public void forEach(Long userId, Consumer<REQ> onUser, Consumer<RES> onAssistant)
    {
        List<ClaudeChatTurn<REQ, RES>> turns = historyMap.get(userId);

        turns.forEach(turn ->
        {
            onUser.accept(turn.getUserMessage());
            if (turn.isClosed()) onAssistant.accept(turn.getAssistantMessage());
        });
    }

    public void clearAll(Long userId)
    {
        List<ClaudeChatTurn<REQ, RES>> turns = historyMap.get(userId);
        turns.clear();
    }
}
