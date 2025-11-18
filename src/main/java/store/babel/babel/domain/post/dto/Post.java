package store.babel.babel.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.babel.babel.domain.tag.dto.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Post
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
    private boolean bookmarked;
    private Long answerCount;
    private Long categoryId;
    private String categoryName;
    private List<Tag> tags;
    private String tagString;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Post stringifyTags()
    {
        this.tagString = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(","));
        return this;
    }
}
