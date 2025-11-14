package store.postHub.postHub.domain.comment.mapper.record;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDetailRecord
{
    private Long id;
    private Long answerId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
}
