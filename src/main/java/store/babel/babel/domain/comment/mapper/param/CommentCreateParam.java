package store.babel.babel.domain.comment.mapper.param;

import lombok.*;
import lombok.experimental.SuperBuilder;
import store.babel.babel.common.BaseEntity;
import store.babel.babel.domain.comment.service.command.CommentCreateCommand;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateParam extends BaseEntity
{
    private Long postId;
    private Long answerId;
    private Long authorId;
    private boolean anonymous;
    private String content;

    public static CommentCreateParam from(CommentCreateCommand command)
    {
        return CommentCreateParam.builder()
                .postId(command.getPostId())
                .answerId(command.getAnswerId())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .content(command.getContent())
                .build();
    }
}
