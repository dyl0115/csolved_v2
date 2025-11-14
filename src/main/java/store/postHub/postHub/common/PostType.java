package store.postHub.postHub.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType
{
    NOTICE(0L, "공지사항"),
    COMMUNITY(1L, "커뮤니티"),
    QUESTION(2L, "면접질문"),
    CODE_REVIEW(3L, "코드리뷰"),
    SUCCESS(4L, "합격수기");

    private final Long code;
    private final String description;
}
