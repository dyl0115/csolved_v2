package store.babel.babel.domain.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Answer
{
    private Long id;
    private Long postId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Answer from(AnswerCreateCommand command)
    {
        return Answer.builder()
                .postId(command.getPostId())
                .authorId(command.getAuthorId())
                .anonymous(command.isAnonymous())
                .content(command.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
