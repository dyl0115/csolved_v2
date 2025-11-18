package store.babel.babel.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.babel.babel.domain.tag.dto.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostCard
{
    private Long id;
    private String title;
    private Long authorId;
    private String authorNickname;
    private boolean anonymous;
    private Long categoryId;
    private String categoryName;
    private Long views;
    private Long likes;
    private Long answerCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Tag> tags;
}
