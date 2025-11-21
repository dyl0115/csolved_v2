package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.post.controller.dto.PostCreateRequest;
import store.babel.babel.domain.tag.dto.Tag;

import java.util.Arrays;
import java.util.List;

import static store.babel.babel.domain.post.dto.PostType.POST;

@Getter
@Builder
public class PostCreateCommand
{
    private Long id;
    private Long postType;
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;
    private Long categoryId;
    private List<Tag> tags;

    public static PostCreateCommand from(PostCreateRequest request)
    {
        return PostCreateCommand.builder()
                .postType(POST.getCode())
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
