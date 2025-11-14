package store.babel.babel.domain.community.service.command;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.community.controller.request.CommunityUpdateRequest;
import store.babel.babel.domain.tag.Tag;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class CommunityUpdateCommand
{
    private String title;
    private String content;
    private Long authorId;
    private boolean anonymous;
    private Long categoryId;
    private List<Tag> tags;

    public static CommunityUpdateCommand from(CommunityUpdateRequest request)
    {
        return CommunityUpdateCommand.builder()
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
