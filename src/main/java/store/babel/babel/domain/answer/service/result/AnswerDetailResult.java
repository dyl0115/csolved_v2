package store.babel.babel.domain.answer.service.result;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.comment.mapper.record.CommentResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AnswerDetailResult
{
    private Long id;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResult> comments;

    public static List<AnswerDetailResult> from(List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answerDetailRecords,
                                                Map<Long, List<CommentResult>> answerCommentListMap)
    {
        return answerDetailRecords.stream()
                .map(record -> groupAnswerWithComments(record, answerCommentListMap.getOrDefault(record.getId(), Collections.emptyList())))
                .toList();
    }

    private static AnswerDetailResult groupAnswerWithComments(store.babel.babel.domain.answer.mapper.record.AnswerDetailResult answerDetailRecord,
                                                              List<CommentResult> comments)
    {
        return AnswerDetailResult.builder()
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