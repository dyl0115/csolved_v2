package store.babel.babel.domain.post.controller.gemini;

import store.babel.babel.domain.post.controller.claude.ChatHistory;
import store.babel.babel.domain.post.controller.claude.LlmClient;
import store.babel.babel.domain.post.controller.claude.PostAssistRequest;
import store.babel.babel.domain.post.controller.claude.PostAssistResponse;

import java.util.function.Consumer;

public class GeminiLlmClient implements LlmClient<PostAssistResponse>
{
    @Override
    public PostAssistResponse stream(ChatHistory<PostAssistRequest, PostAssistResponse> history, Consumer<String> onText)
    {
        return null;
    }
}
