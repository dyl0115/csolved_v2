package store.postHub.postHub.domain.community.mapper.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.postHub.postHub.common.Post;
import store.postHub.postHub.domain.community.service.command.CommunityCreateCommand;
import store.postHub.postHub.domain.community.service.command.CommunityUpdateCommand;
import store.postHub.postHub.domain.tag.Tag;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRecord extends Post
{
    private Long categoryId;
    private String categoryName;
    private List<Tag> tags;

    public static CommunityRecord from(CommunityCreateCommand command)
    {
        return CommunityRecord.builder()
                .title(command.getTitle())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .categoryId(command.getCategoryId())
                .tags(command.getTags())
                .build();
    }

    public static CommunityRecord from(CommunityUpdateCommand command)
    {
        return CommunityRecord.builder()
                .title(command.getTitle())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .categoryId(command.getCategoryId())
                .tags(command.getTags())
                .build();
    }
}
