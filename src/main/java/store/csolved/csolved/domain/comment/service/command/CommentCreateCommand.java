package store.csolved.csolved.domain.comment.service.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.comment.controller.request.CommentCreateRequest;

@Getter
@Builder
public class CommentCreateCommand
{
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
