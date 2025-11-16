package store.babel.babel.domain.answer.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequest
{
    @NotNull
    private Long postId;

    @NotNull
    private Long authorId;

    @NotNull
    private Boolean anonymous;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;
}
