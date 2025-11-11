package store.csolved.csolved.domain.notice.mapper.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.csolved.csolved.common.Post;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.domain.community.service.command.CommunityCreateCommand;
import store.csolved.csolved.domain.community.service.command.CommunityUpdateCommand;
import store.csolved.csolved.domain.notice.service.command.NoticeCreateCommand;
import store.csolved.csolved.domain.notice.service.command.NoticeUpdateCommand;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Notice extends Post
{
    public static Notice from(NoticeCreateCommand command)
    {
        return Notice.builder()
                .title(command.getTitle())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .build();
    }

    public static Notice from(NoticeUpdateCommand command)
    {
        return Notice.builder()
                .title(command.getTitle())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .build();
    }
}
