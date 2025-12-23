package store.babel.babel.global.llm.gemini;

import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.domain.assistant.dto.PostAssistResponse;
import store.babel.babel.domain.assistant.session.AssistantChatSession;
import store.babel.babel.global.llm.claude.ClaudeChatHistoryManager;
import store.babel.babel.global.llm.LlmClient;

import java.util.function.Consumer;

public class GeminiLlmClient implements LlmClient<PostAssistResponse>
{
    @Override
    public void stream(AssistantChatSession chatSession, PostAssistResponse request)
    {
        
    }

    @Override
    public void cleanUp(Long userId)
    {

    }
}
