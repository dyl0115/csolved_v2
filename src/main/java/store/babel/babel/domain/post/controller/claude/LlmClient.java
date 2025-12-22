package store.babel.babel.domain.post.controller.claude;

import java.util.function.Consumer;

/**
 * LLM 클라이언트 인터페이스
 * Claude, GPT 등 다양한 LLM 구현체로 교체 가능
 */
public interface LlmClient<RES>
{
    /**
     * 스트리밍 방식으로 LLM 호출
     *
     * @param history 대화 히스토리
     * @param onText 스트리밍 텍스트를 받을 때마다 호출되는 콜백
     * @return 파싱된 응답 객체
     */
    RES stream(ChatHistory<PostAssistRequest, RES> history, Consumer<String> onText);
}
