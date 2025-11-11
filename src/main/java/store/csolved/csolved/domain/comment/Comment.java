package store.csolved.csolved.domain.comment;

import lombok.*;
import lombok.experimental.SuperBuilder;
import store.csolved.csolved.common.BaseEntity;
import store.csolved.csolved.domain.comment.service.command.CommentCreateCommand;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity
{
    private Long postId;
    private Long answerId;
    private Long authorId;
    private String authorProfileImage; // 이부분은 지울 것.
    private String authorNickname; // 여기도 이후에 지울것.
    private boolean anonymous;
    private String content;

    public static Comment from(CommentCreateCommand command)
    {
        return Comment.builder()
                .postId(command.getPostId())
                .answerId(command.getAnswerId())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .content(command.getContent())
                .build();
    }
}
