package store.babel.babel.global.llm.gemini;


import com.google.common.collect.ImmutableMap;
import com.google.genai.Chat;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.errors.ClientException;
import com.google.genai.types.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.domain.assistant.dto.PostAssistResponse;
import store.babel.babel.domain.assistant.session.AssistantChatSession;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.llm.PromptManager;
import store.babel.babel.global.llm.LlmClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@Component
public class GeminiLlmClient implements LlmClient<PostAssistRequest>
{
    private static final String SYSTEM_PROMPT = "prompts/post-create-system.md";
    private static final String USER_PROMPT = "prompts/post-create-user.md";

    private final GeminiChatHistoryManager<PostAssistRequest, PostAssistResponse> historyManager;
    private final Client googleClient;
    private final PromptManager promptManager;

    @Override
    public void openSession(Long userId)
    {
        historyManager.createHistory(userId,
                () -> googleClient.chats.create("gemini-2.0-flash-lite"));

    }

    public void test()
    {
        List<Content> history = new ArrayList<>();

        history.add(Content.builder()
                .role("user")
                .parts(Part.builder().text("안녕 너는 누구니?").build())
                .build());

        history.add(Content.builder()
                .role("model")
                .parts(Part.builder().text("저는 Google에서 개발한 Gemini입니다.").build())
                .build());

    }

    @Override
    public void stream(AssistantChatSession chatSession, PostAssistRequest request)
    {
        Schema schema = Schema.builder()
                .type("object")
                .properties(Map.of(
                        "role", Schema.builder()
                                .type("string")
                                .description("이 값은 항상 assistant")
                                .build(),
                        "title", Schema.builder().type("string").build(),
                        "tags", Schema.builder()
                                .type("array")
                                .items(Schema.builder().type("string").build())
                                .build(),
                        "content", Schema.builder().type("string").build(),
                        "message", Schema.builder().type("string").build()))
                .required(List.of("role", "message"))
                .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(schema)
                .systemInstruction(
                        Content.fromParts(Part.fromText(promptManager.load(SYSTEM_PROMPT))))
                .candidateCount(1)
                .maxOutputTokens(1024)
                .build();

        Chat session = historyManager.getHistory(request.getAuthorId());

        try (ResponseStream<GenerateContentResponse> streamResponse
                     = session.sendMessageStream(promptManager.loadAndRender(USER_PROMPT, request), config))
        {
            streamResponse
                    .forEach(response ->
                    {
                        log.info(response.text());
                        chatSession.send(response.text());
                    });
        }
        catch (ClientException exception)
        {
            if (exception.code() == 429)
            {
                throw new BabelException(ExceptionCode.AI_RESOURCE_EXHAUSTED);
            }
        }
    }

    @Override
    public void closeSession(Long userId)
    {
        historyManager.clearAll(userId);
    }
}
