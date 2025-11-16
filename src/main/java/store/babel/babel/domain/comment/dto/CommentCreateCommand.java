package store.babel.babel.domain.comment.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import store.babel.babel.domain.comment.controller.dto.CommentCreateRequest;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateCommand
{
    private Long id;
    private Long postId;
    private Long answerId;
    private Long authorId;
    private boolean anonymous;
    private String content;

    public static CommentCreateCommand from(CommentCreateRequest request)
    {
        return CommentCreateCommand.builder()
                .postId(request.getPostId())
                .answerId(request.getAnswerId())
                .authorId(request.getAuthorId())
                .anonymous(request.isAnonymous())
                .content(request.getContent())
                .build();
    }
}
