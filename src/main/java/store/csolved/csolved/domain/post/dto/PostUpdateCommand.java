package store.csolved.csolved.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.post.controller.dto.PostUpdateRequest;
import store.csolved.csolved.domain.tag.dto.Tag;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class PostUpdateCommand
{
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;
    private Long categoryId;
    private List<Tag> tags;

    public static PostUpdateCommand from(PostUpdateRequest request)
    {
        return PostUpdateCommand.builder()
                .id(request.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .categoryId(request.getCategoryId())
                .tags(convertStringToTags(request.getTags()))
                .build();
    }

    private static List<Tag> convertStringToTags(String tagString)
    {
        return Arrays.stream(tagString.split(","))
                .map(Tag::from)
                .toList();
    }
}
