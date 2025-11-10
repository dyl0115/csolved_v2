package store.csolved.csolved.domain.answer.mapper.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import store.csolved.csolved.common.BaseEntity;
import store.csolved.csolved.domain.answer.service.command.AnswerCreateCommand;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseEntity
{
    private Long postId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
//    private Long totalScore;
//    private Long voterCount;

    public static Answer from(AnswerCreateCommand command)
    {
        return Answer.builder()
                .postId(command.getPostId())
                .authorId(command.getAuthorId())
                .anonymous(command.getAnonymous())
                .content(command.getContent())
                .build();
    }
}
