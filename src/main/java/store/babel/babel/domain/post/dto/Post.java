package store.babel.babel.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.babel.babel.domain.tag.Tag;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
