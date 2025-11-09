package store.csolved.csolved.domain.answer;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.comment.Comment;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AnswerWithComments
{
    private Long id;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
    private List<Comment> comments;

    public static AnswerWithComments from(Answer answer,
                                          List<Comment> comments)
    {
        return AnswerWithComments.builder()
                .id(answer.getId())
                .authorId(answer.getAuthorId())
                .authorProfileImage(answer.getAuthorProfileImage())
                .authorNickname(answer.getAuthorNickname())
                .anonymous(answer.isAnonymous())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .comments(comments)
                .build();
    }

    public static List<AnswerWithComments> from(List<Answer> answers,
                                                Map<Long, List<Comment>> commentMap)
    {
        return answers.stream()
                .map(
                        answer ->
                        {
                            Long answerId = answer.getId();
                            List<Comment> comments = commentMap.getOrDefault(answerId, null);
                            if (comments != null)
                            {
                                return AnswerWithComments.from(answer, comments);
                            }
                            return AnswerWithComments.from(answer, Collections.emptyList());
                        }
                ).toList();
    }
}