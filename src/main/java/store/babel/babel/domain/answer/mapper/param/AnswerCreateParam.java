package store.babel.babel.domain.answer.mapper.param;

import lombok.*;
import lombok.experimental.SuperBuilder;
import store.babel.babel.common.BaseEntity;
import store.babel.babel.domain.answer.service.command.AnswerCreateCommand;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateParam extends BaseEntity
{
    private Long postId;
    private Long authorId;
    private boolean anonymous;
    private String content;

    public static AnswerCreateParam from(AnswerCreateCommand command)
    {
        return AnswerCreateParam.builder()
                .postId(command.getPostId())
                .authorId(command.getAuthorId())
                .anonymous(command.getAnonymous())
                .content(command.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
