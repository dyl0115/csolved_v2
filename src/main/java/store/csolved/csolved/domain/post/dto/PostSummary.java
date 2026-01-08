package store.csolved.csolved.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSummary
{
    private Long postId;
    private String title;
    private Integer views;
    private Integer likes;
    private Integer answerCount;
    private LocalDateTime createdAt;
    private Long categoryId;

}
