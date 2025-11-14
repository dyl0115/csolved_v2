package store.postHub.postHub.domain.notice.mapper.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.postHub.postHub.common.Post;
import store.postHub.postHub.domain.notice.service.command.NoticeCreateCommand;
import store.postHub.postHub.domain.notice.service.command.NoticeUpdateCommand;

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
