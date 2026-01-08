package store.csolved.csolved.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Comment
{
    private Long id;
    private Long postId;
    private Long answerId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
