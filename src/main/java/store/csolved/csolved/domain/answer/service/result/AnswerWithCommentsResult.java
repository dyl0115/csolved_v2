package store.csolved.csolved.domain.answer.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.mapper.record.AnswerDetailRecord;
import store.csolved.csolved.domain.comment.mapper.param.CommentCreateParam;
import store.csolved.csolved.domain.comment.mapper.record.CommentDetailRecord;
import store.csolved.csolved.domain.comment.service.result.CommentDetailResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AnswerWithCommentsResult
{
    private Long id;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentDetailResult> comments;

    public static List<AnswerWithCommentsResult> from(List<AnswerDetailRecord> answerDetailRecords,
                                                      Map<Long, List<CommentDetailRecord>> answerCommentListMap)
    {
        return answerDetailRecords.stream()
                .map(record -> groupAnswerWithComments(record, answerCommentListMap.getOrDefault(record.getId(), Collections.emptyList())))
                .toList();
    }

    private static AnswerWithCommentsResult groupAnswerWithComments(AnswerDetailRecord answerDetailRecord,
                                                                    List<CommentDetailRecord> comments)
    {
        return AnswerWithCommentsResult.builder()
                .id(answerDetailRecord.getId())
                .authorId(answerDetailRecord.getAuthorId())
                .authorProfileImage(answerDetailRecord.getAuthorProfileImage())
                .authorNickname(answerDetailRecord.getAuthorNickname())
                .anonymous(answerDetailRecord.isAnonymous())
                .content(answerDetailRecord.getContent())
                .createdAt(answerDetailRecord.getCreatedAt())
                .comments(CommentDetailResult.from(comments))
                .build();
    }
}