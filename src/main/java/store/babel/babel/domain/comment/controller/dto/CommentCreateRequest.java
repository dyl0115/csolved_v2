package store.babel.babel.domain.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import store.babel.babel.domain.comment.dto.CommentCreateCommand;

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

    public CommentCreateCommand toComment()
    {
        return CommentCreateCommand.builder()
                .postId(postId)
                .answerId(answerId)
                .authorId(authorId)
                .anonymous(anonymous)
                .content(content)
                .build();
    }
}
