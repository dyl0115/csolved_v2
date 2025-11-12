package store.csolved.csolved.domain.answer.service.command;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.controller.request.AnswerCreateRequest;

import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerCreateCommand
{
    private Long postId;
    private Long authorId;
    private Boolean anonymous;
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
