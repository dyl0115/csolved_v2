package store.babel.babel.global.llm;

import store.babel.babel.domain.assistant.session.AssistantChatSession;


// LLM 클라이언트 인터페이스
// Claude, GPT, Gemini 등 다양한 LLM 구현체로 교체 가능
public interface LlmClient<REQ>
{
    // LLM 클라이언트는 세션에 저장된 SseEmitter를 통해
    // 응답을 스트리밍 방식으로 사용자에게 전달한다.
    void stream(AssistantChatSession chatSession, REQ request);

    // LLM 클라이언트별 대화 히스토리 등 자원을 정리한다.
    // (ex1: Claude 대화 히스토리 삭제)
    // (ex2: Gemini Chat 객체 제거)
    void cleanUp(Long userId);
}
