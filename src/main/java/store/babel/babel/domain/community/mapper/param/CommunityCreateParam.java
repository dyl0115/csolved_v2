package store.babel.babel.domain.community.mapper.param;

import lombok.*;
import store.babel.babel.domain.community.service.command.CommunityCreateCommand;
import store.babel.babel.domain.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;

import static store.babel.babel.common.PostType.COMMUNITY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCreateParam
{
    private Long id;
    private String postType;
    private String title;
    private boolean anonymous;
    private Long authorId;
    private String authorNickname;
    private String content;
    private Long views;
    private Long likes;
    private Long answerCount;
    private Long categoryId;
    private String categoryName;
    private List<Tag> tags;
    private LocalDateTime createdAt;

    public static CommunityCreateParam from(CommunityCreateCommand command)
    {
        return CommunityCreateParam.builder()
                .postType(COMMUNITY.getCode().toString())
                .title(command.getTitle())
                .content(command.getContent())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .categoryId(command.getCategoryId())
                .tags(command.getTags())
                .build();
    }
}
