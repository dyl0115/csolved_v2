package store.babel.babel.domain.post.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.helpers.BetaMessageAccumulator;
import com.anthropic.models.beta.messages.*;
import com.anthropic.models.messages.Model;
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
        log.info(message.toString());
        ClaudeSession session = claudeSessionManager.getSession(userId);
        SseEmitter emitter = session.getEmitter();

        session.addHistory(message);

        log.info("history: " + session.getHistory().toString());
        log.info("emitter: " + emitter);
        List<ClaudeMessage> history = session.getHistory();
        BetaMessageAccumulator accumulator = BetaMessageAccumulator.create();

        try (StreamResponse<BetaRawMessageStreamEvent> streamResponse
                     = claudeClient.beta().messages().createStreaming(createParams(history)))
        {
            streamResponse.stream()
                    .peek(accumulator::accumulate)
                    .forEach(
                            event ->
                            {
                                if (event.contentBlockDelta().isPresent())
                                {
                                    event.contentBlockDelta().stream()
                                            .flatMap(contentBlock -> contentBlock.delta().text().stream())
                                            .map(BetaTextDelta::text)
                                            .forEach(text ->
                                            {
                                                try
                                                {
                                                    log.info("text: " + text);
                                                    emitter.send(SseEmitter.event()
                                                            .name("message")
                                                            .data(text));
                                                }
                                                catch (IOException e)
                                                {
                                                    throw new RuntimeException(e);
                                                }
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
        session.addHistory(claudeMessage);
    }

    private StructuredMessageCreateParams<ClaudeMessage> createParams(List<ClaudeMessage> history)
    {
        StructuredMessageCreateParams.Builder<ClaudeMessage> builder = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5_20251001)
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
                당신은 게시글 작성을 도와주는 에이전트입니다.
                
                현재 사용자의 게시글 상태:
                - 역할: %s
                - 제목: %s
                - 내용(Quill Delta): %s
                - 태그: %s
                
                사용자 메시지: %s
                
                ## 응답 규칙
                1. 제목, 내용, 태그는 필요한 것만 수정하세요.
                2. 메시지는 반드시 작성하세요.
                3. **내용(content)은 반드시 Quill Delta JSON 형식으로 작성하세요.**
                
                ## Quill Delta 형식 예시
                {"ops":[{"insert":"굵은 텍스트","attributes":{"bold":true}},{"insert":"\\n일반 텍스트\\n"}]}
                
                ## ⚠️ 미디어 처리 규칙
                **이미지:**
                - 사용자가 직접 업로드해야 함
                - message: "에디터의 이미지 버튼으로 직접 업로드해주세요"
                - content: 회색 이탤릭체로 위치 표시
                예: {"ops":[{"insert":"[이미지 위치]\\n","attributes":{"color":"#888","italic":true}}]}
                
                **동영상:**
                - 유튜브 URL 받은 경우에만 삽입 가능
                - URL 없으면 message: "유튜브 링크를 알려주세요"
                - URL 있으면 embed 형식으로 변환: watch?v= → embed/
                예: {"insert":{"video":"https://www.youtube.com/embed/VIDEO_ID"}}
                
                ## 주의사항
                - 줄바꿈은 반드시 \\n으로 표현
                - 기존 미디어는 그대로 유지
                - 임의의 URL 절대 사용 금지
                
                내용을 수정할 때는 위 형식을 절대 벗어나지 마세요.
                """.formatted(
                message.getRole(),
                message.getTitle(),
                message.getContent(),
                message.getTags().toString(),
                message.getMessage());
    }
}
