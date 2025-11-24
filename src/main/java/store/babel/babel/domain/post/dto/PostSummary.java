package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSummary
{
    private Long postId;
    private String title;
    private Integer viewCount;
    private Integer likeCount;
    private Integer answerCount;

}
