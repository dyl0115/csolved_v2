package store.csolved.csolved.domain.answer.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor  // 추가!
@AllArgsConstructor
public class AnswerCreateRequest
{
    private Long postId;
    private Long authorId;
    private Boolean anonymous;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    public static AnswerCreateRequest empty()
    {
        return AnswerCreateRequest.builder().build();
    }
}
