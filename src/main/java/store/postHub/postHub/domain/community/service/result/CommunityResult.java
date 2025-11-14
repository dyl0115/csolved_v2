package store.postHub.postHub.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.domain.community.mapper.record.CommunityRecord;
import store.postHub.postHub.domain.tag.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommunityResult
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
    private String tags;

    public static CommunityResult from(CommunityRecord community)
    {
        return CommunityResult.builder()
                .id(community.getId())
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
                .tags(tagsToString(community.getTags()))
                .build();
    }

    private static String tagsToString(List<Tag> tags)
    {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(","));
    }
}
