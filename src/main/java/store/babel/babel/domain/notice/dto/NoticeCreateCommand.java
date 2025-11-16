package store.babel.babel.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.notice.controller.dto.NoticeCreateRequest;

import static store.babel.babel.common.PostType.NOTICE;

@Getter
@Builder
public class NoticeCreateCommand
{
    private Long postType;
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;

    public static NoticeCreateCommand from(NoticeCreateRequest request)
    {
        return NoticeCreateCommand.builder()
                .postType(NOTICE.getCode())
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .build();
    }
}
