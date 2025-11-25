package store.babel.babel.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType
{
    NOTICE(0L, "공지사항"),
    POST(1L, "게시글");

    private final Long value;
    private final String description;
}
