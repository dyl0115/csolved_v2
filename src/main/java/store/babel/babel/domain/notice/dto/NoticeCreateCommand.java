package store.babel.babel.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.notice.controller.dto.NoticeCreateRequest;

import static store.babel.babel.domain.post.dto.PostType.NOTICE;

@Getter
@Builder
public class NoticeCreateCommand
{
    private Long id;
    private Long postType;
    private boolean anonymous;
    private Long categoryId;
    private Long authorId;
    private String title;
    private String content;

    public static NoticeCreateCommand from(NoticeCreateRequest request)
    {
        return NoticeCreateCommand.builder()
                .postType(NOTICE.getCode())
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .categoryId(request.getCategoryId())
                .build();
    }
}
