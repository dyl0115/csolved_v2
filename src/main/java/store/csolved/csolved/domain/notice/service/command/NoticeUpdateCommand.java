package store.csolved.csolved.domain.notice.service.command;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.notice.controller.request.NoticeCreateRequest;
import store.csolved.csolved.domain.notice.controller.request.NoticeUpdateRequest;

@Getter
@Builder
public class NoticeUpdateCommand
{
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;

    public static NoticeUpdateCommand from(NoticeUpdateRequest request)
    {
        return NoticeUpdateCommand.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .build();
    }
}
