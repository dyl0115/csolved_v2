package store.babel.babel.domain.answer.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.comment.dto.Comment;

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
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private List<Comment> comments;

    public static List<AnswerWithComments> from(List<Answer> answers,
                                                Map<Long, List<Comment>> answerCommentListMap)
    {
        return answers.stream()
                .map(answer -> groupAnswerWithComments(answer, answerCommentListMap.getOrDefault(answer.getId(), Collections.emptyList())))
                .toList();
    }

    private static AnswerWithComments groupAnswerWithComments(Answer answer,
                                                              List<Comment> comments)
    {
        return AnswerWithComments.builder()
                .id(answer.getId())
                .authorId(answer.getAuthorId())
                .authorProfileImage(answer.getAuthorProfileImage())
                .authorNickname(answer.getAuthorNickname())
                .anonymous(answer.isAnonymous())
                .content(answer.getContent())
                .likes(answer.getLikes())
                .createdAt(answer.getCreatedAt())
                .deletedAt(answer.getDeletedAt())
                .comments(comments)
                .build();
    }
}