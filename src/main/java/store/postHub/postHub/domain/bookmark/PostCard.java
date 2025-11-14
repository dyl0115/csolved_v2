package store.postHub.postHub.domain.bookmark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.postHub.postHub.common.BaseEntity;
import store.postHub.postHub.domain.tag.Tag;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostCard extends BaseEntity
{
    private Long postId;
    private Long postType;
    private String title;
    private boolean anonymous;
    private Long authorId;
    private String authorNickname;
    private Long categoryId;
    private String categoryName;
    private List<Tag> tags;
    private Long views;
    private Long likes;
    private Long answerCount;
}
