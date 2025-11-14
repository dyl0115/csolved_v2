package store.postHub.postHub.domain.comment.service.command;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.domain.comment.controller.request.CommentCreateRequest;

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
