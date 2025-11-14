package store.babel.babel.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.babel.babel.domain.tag.Tag;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostCard
{
    private Long id;
    private String postType;
    private String title;
    private boolean anonymous;
    private Long authorId;
    private String authorNickname;
    private Long views;
    private Long likes;
    private Long answerCount;
    private Long categoryId;
    private String categoryName;
    private List<Tag> tags;
}
