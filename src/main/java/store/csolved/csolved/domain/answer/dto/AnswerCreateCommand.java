package store.csolved.csolved.domain.answer.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import store.csolved.csolved.domain.answer.controller.dto.AnswerCreateRequest;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateCommand
{
    private Long id;
    private Long postId;
    private Long authorId;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;

    public static AnswerCreateCommand from(AnswerCreateRequest request)
    {
        return AnswerCreateCommand.builder()
                .postId(request.getPostId())
                .authorId(request.getAuthorId())
                .anonymous(request.getAnonymous())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
