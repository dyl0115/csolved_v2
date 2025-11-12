package store.csolved.csolved.domain.answer.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;

import java.time.LocalDateTime;

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
