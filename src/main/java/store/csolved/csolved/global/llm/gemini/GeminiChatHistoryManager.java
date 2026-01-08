package store.csolved.csolved.global.llm.gemini;

import com.google.genai.Chat;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Component
public class GeminiChatHistoryManager<REQ, RES>
{
    private Map<Long, Chat> historyMap;

    @PostConstruct
    public void init()
    {
        historyMap = new ConcurrentHashMap<>();
    }

    public void createHistory(Long userId, Supplier<Chat> supplier)
    {
        historyMap.put(userId, supplier.get());
    }

    public Chat getHistory(Long userId)
    {
        if (!hasHistory(userId))
        {
            log.error("Gemini 대화 히스토리 세션이 없음 userId={}", userId);
            throw new RuntimeException("Gemini 대화 히스토리 세션이 없음");
        }
        return historyMap.get(userId);
    }

    public boolean hasHistory(Long userId)
    {
        return historyMap.get(userId) != null;
    }

    public void clearAll(Long userId)
    {
        historyMap.remove(userId);
    }
}
