package store.csolved.csolved.domain.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Answer
{
    private Long id;
    private Long postId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

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

    public void maskContent(String deletedContentMessage)
    {
        if (isDeleted()) this.content = deletedContentMessage;
    }

    public boolean isDeleted()
    {
        return this.deletedAt != null;
    }
}
