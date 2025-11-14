package store.babel.babel.domain.notice.service.command;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.notice.controller.request.NoticeCreateRequest;

@Getter
@Builder
public class NoticeCreateCommand
{
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;

    public static NoticeCreateCommand from(NoticeCreateRequest request)
    {
        return NoticeCreateCommand.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .build();
    }
}
