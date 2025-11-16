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
    private LocalDateTime createdAt;
    private List<Comment> comments;

    public static List<AnswerWithComments> from(List<Answer> answerDetailRecords,
                                                Map<Long, List<Comment>> answerCommentListMap)
    {
        return answerDetailRecords.stream()
                .map(record -> groupAnswerWithComments(record, answerCommentListMap.getOrDefault(record.getId(), Collections.emptyList())))
                .toList();
    }

    private static AnswerWithComments groupAnswerWithComments(Answer answerDetailRecord,
                                                              List<Comment> comments)
    {
        return AnswerWithComments.builder()
                .id(answerDetailRecord.getId())
                .authorId(answerDetailRecord.getAuthorId())
                .authorProfileImage(answerDetailRecord.getAuthorProfileImage())
                .authorNickname(answerDetailRecord.getAuthorNickname())
                .anonymous(answerDetailRecord.isAnonymous())
                .content(answerDetailRecord.getContent())
                .createdAt(answerDetailRecord.getCreatedAt())
                .comments(comments)
                .build();
    }
}