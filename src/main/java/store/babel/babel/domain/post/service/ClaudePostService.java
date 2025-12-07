package store.babel.babel.domain.post.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.helpers.BetaMessageAccumulator;
import com.anthropic.models.beta.messages.*;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.RawMessageStreamEvent;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.post.controller.claude.ClaudeMessage;
import store.babel.babel.domain.post.controller.claude.ClaudeSession;
import store.babel.babel.domain.post.controller.claude.ClaudeSessionManager;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClaudePostService
{
    private final ClaudeSessionManager claudeSessionManager;
    private final AnthropicClient claudeClient;

    public SseEmitter connect(Long userId)
    {
        claudeSessionManager.createSession(userId);
        return claudeSessionManager.getSession(userId).getEmitter();
    }

    @Async
    public void stream(Long userId, ClaudeMessage message) throws IOException
    {
        ClaudeSession session = claudeSessionManager.getSession(userId);
        SseEmitter emitter = session.getEmitter();

        session.addHistory(message);
        List<ClaudeMessage> history = session.getHistory();

        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();


        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse
                     = claudeClient.beta().messages().createStreaming(createParams(history)))
        {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper();
            StringBuilder jsonBuffer = new StringBuilder();

            streamResponse.stream()
                    .peek(accumulator::accumulate)
                    .forEach(
                            event ->
                            {
                                if (event.contentBlockDelta().isPresent())
                                {
                                    event.contentBlockDelta().stream()
                                            .flatMap(contentBlock -> contentBlock.delta()
                                                    .text()
                                                    .stream())
                                            .map(BetaTextDelta::text)
                                            .forEach(chunk ->
                                            {
                                                // 여기부터 불완전한 파싱 시작
                                                jsonBuffer.append(chunk);

                                                try (JsonParser parser = factory.createParser(jsonBuffer.toString()))
                                                {
                                                    while (parser.nextToken() != null)
                                                    {
                                                        if (parser.currentToken() == JsonToken.FIELD_NAME)
                                                        {
                                                            String fieldName = parser.getCurrentName();
                                                            parser.nextToken(); // 값으로 이동

                                                            switch (fieldName)
                                                            {
                                                                case "title", "content", "message", "role" ->
                                                                {
                                                                    if (parser.currentToken() == JsonToken.VALUE_STRING)
                                                                    {
                                                                        // ✅ 필드별로 개별 JSON 생성
                                                                        Map<String, Object> fieldData = new HashMap<>();
                                                                        fieldData.put(fieldName, chunk);

                                                                        log.info("Field update: " + chunk);
                                                                        emitter.send(SseEmitter.event()
                                                                                .name("message")
                                                                                .data(fieldData)  // {content: "..."} 또는 {title: "..."}
                                                                                .build());
                                                                    }
                                                                }
                                                                case "tags" ->
                                                                {
                                                                    if (parser.currentToken() == JsonToken.START_ARRAY)
                                                                    {
                                                                        List<String> tags = new ArrayList<>();
                                                                        while (parser.nextToken() != JsonToken.END_ARRAY)
                                                                        {
                                                                            if (parser.currentToken() == JsonToken.VALUE_STRING)
                                                                            {
                                                                                tags.add(parser.getText());
                                                                            }
                                                                        }

                                                                        // ✅ tags도 개별 JSON으로
                                                                        Map<String, Object> fieldData = new HashMap<>();
                                                                        fieldData.put("tags", tags);

                                                                        log.info("Field update: " + fieldData);
                                                                        emitter.send(SseEmitter.event()
                                                                                .name("message")
                                                                                .data(fieldData)  // {tags: [...]}
                                                                                .build());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                catch (com.fasterxml.jackson.core.JsonParseException e)
                                                {
                                                    log.debug("Incomplete JSON, waiting...");
                                                }
                                                catch (IOException e)
                                                {
                                                    throw new RuntimeException(e);
                                                }

//                                                try
//                                                {
//                                                    JsonParser parser = factory.createParser(jsonBuffer.toString());
//                                                    Map<String, Object> currentState = new HashMap<>();
//
//                                                    while (parser.nextToken() != null)
//                                                    {
//                                                        JsonToken token = parser.currentToken();
//
//                                                        // 필드 이름을 만났을 때,
//                                                        if (token == JsonToken.FIELD_NAME)
//                                                        {
//                                                            String fieldName = parser.currentName();
//
//                                                            // 다음 토큰(값)으로 이동
//                                                            parser.nextToken();
//
//                                                            switch (fieldName)
//                                                            {
//                                                                case "title" ->
//                                                                {
//                                                                    if (parser.currentToken() == JsonToken.VALUE_STRING)
//                                                                    {
//                                                                        currentState.put("title", parser.getText());
//                                                                    }
//                                                                }
//
//                                                                case "content" ->
//                                                                {
//                                                                    if (parser.currentToken() == JsonToken.VALUE_STRING)
//                                                                    {
//                                                                        currentState.put("content", parser.getText());
//                                                                    }
//                                                                }
//
//                                                                case "tags" ->
//                                                                {
//                                                                    if (parser.currentToken() == JsonToken.START_ARRAY)
//                                                                    {
//                                                                        List<String> tags = new ArrayList<>();
//                                                                        while (parser.nextToken() != JsonToken.END_ARRAY)
//                                                                        {
//                                                                            if (parser.currentToken() == JsonToken.VALUE_STRING)
//                                                                            {
//                                                                                tags.add(parser.getText());
//                                                                            }
//                                                                        }
//                                                                        currentState.put("tags", tags);
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                        parser.close();
//
//                                                        try
//                                                        {
//                                                            log.info(currentState.toString());
//                                                            emitter.send(SseEmitter.event()
//                                                                    .name("message")
//                                                                    .data(currentState)
//                                                                    .build());
//                                                        }
//                                                        catch (IOException e)
//                                                        {
//                                                            throw new RuntimeException(e);
//                                                        }
//                                                    }
//                                                }
//                                                catch (IOException e)
//                                                {
//                                                    throw new RuntimeException(e);
//                                                }


                                            });
                                }
                            }
                    );
        }

        String jsonText = accumulator.message(ClaudeMessage.class)
                .content()
                .stream()
                .flatMap(block -> block.rawContentBlock().text().stream())
                .map(BetaTextBlock::text)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Text Content"));

        ObjectMapper objectMapper = new ObjectMapper();

        ClaudeMessage claudeMessage = objectMapper.readValue(jsonText, ClaudeMessage.class);
        System.out.println(claudeMessage);
    }

    private StructuredMessageCreateParams<ClaudeMessage> createParams(List<ClaudeMessage> history)
    {
        StructuredMessageCreateParams.Builder<ClaudeMessage> builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_SONNET_4_5_20250929)
                .maxTokens(2048L)
                .outputFormat(ClaudeMessage.class);

        history.forEach(message ->
        {
            if (Objects.equals(message.getRole(), "assistant"))
            {
                builder.addAssistantMessage(createStringMessage(message));
            }
            else if (Objects.equals(message.getRole(), "user"))
            {
                builder.addUserMessage(createStringMessage(message));
            }
        });

        return builder.build();
    }

    private String createStringMessage(ClaudeMessage message)
    {
        return """
                클로드 당신은 게시글 작성을 도와주는 에이전트 입니다.
                현재 사용자는 다음과 같이 게시글을 작성한 상황입니다.
                역할: %s
                제목: %s
                내용: %s
                태그: %s
                메시지: %s
                당신이 사용자의 메시지를 읽고
                적절하게 제목, 내용, 태그를 수정하고 사용자에게 하고 싶은 말을 메시지에 담아 
                응답하세요.
                제목, 내용, 태그를 모두 작성할 필요는 없습니다. 
                당신이 상황을 보고 3개 모두 수정해도 좋고, 일부만 수정하고 메시지를 작성해도 좋습니다.
                단 메시지는 꼭 작성하세요.
                """.formatted(message.getRole(),
                message.getTitle(),
                message.getContent(),
                message.getTags().toString(),
                message.getMessage());
    }
}
