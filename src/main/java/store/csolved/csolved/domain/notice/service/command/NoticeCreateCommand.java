package store.csolved.csolved.domain.notice.service.command;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.community.controller.request.CommunityCreateRequest;
import store.csolved.csolved.domain.community.service.command.CommunityCreateCommand;
import store.csolved.csolved.domain.notice.controller.request.NoticeCreateRequest;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;
import store.csolved.csolved.domain.tag.Tag;

import java.util.List;

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
