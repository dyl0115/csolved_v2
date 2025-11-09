package store.csolved.csolved.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.domain.tag.Tag;

import java.util.List;

@Getter
@Builder
public class CommunityResult
{
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

    public static CommunityResult from(Community community)
    {
        return CommunityResult.builder()
                .postType(community.getPostType())
                .title(community.getTitle())
                .anonymous(community.isAnonymous())
                .authorId(community.getAuthorId())
                .authorNickname(community.getAuthorNickname())
                .content(community.getContent())
                .views(community.getViews())
                .likes(community.getLikes())
                .answerCount(community.getAnswerCount())
                .categoryId(community.getCategoryId())
                .categoryName(community.getCategoryName())
                .tags(community.getTags())
                .build();
    }
}
