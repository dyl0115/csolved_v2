package store.postHub.postHub.domain.answer.mapper.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.postHub.postHub.common.BaseEntity;
import store.postHub.postHub.domain.answer.service.command.AnswerCreateCommand;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailRecord extends BaseEntity
{
    private Long postId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;

    public static AnswerDetailRecord from(AnswerCreateCommand command)
    {
        return AnswerDetailRecord.builder()
                .postId(command.getPostId())
                .authorId(command.getAuthorId())
                .anonymous(command.getAnonymous())
                .content(command.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
