package store.babel.babel.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.notice.controller.dto.NoticeUpdateRequest;

@Getter
@Builder
public class NoticeUpdateCommand
{
    private Long noticeId;
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;

    public static NoticeUpdateCommand from(NoticeUpdateRequest request)
    {
        return NoticeUpdateCommand.builder()
                .noticeId(request.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .build();
    }
}
