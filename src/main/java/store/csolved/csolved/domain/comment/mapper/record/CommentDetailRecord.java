package store.csolved.csolved.domain.comment.mapper.record;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.comment.mapper.param.CommentCreateParam;
import store.csolved.csolved.domain.comment.service.command.CommentCreateCommand;

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
