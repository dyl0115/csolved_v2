package store.csolved.csolved.domain.comment.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import store.csolved.csolved.domain.comment.Comment;

@Builder
@Data
public class CommentCreateRequest
{
    private Long postId;
    private Long answerId;
    private Long authorId;

    @NotNull
    private boolean anonymous;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public static CommentCreateRequest empty()
    {
        return CommentCreateRequest.builder()
                .build();
    }

    public Comment toComment()
    {
        return Comment.builder()
                .postId(postId)
                .answerId(answerId)
                .authorId(authorId)
                .anonymous(anonymous)
                .content(content)
                .build();
    }
}
