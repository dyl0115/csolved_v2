package store.csolved.csolved.domain.comment.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.comment.mapper.record.CommentDetailRecord;

import java.util.List;

@Getter
@Builder
public class CommentDetailResult
{
    private Long postId;
    private Long answerId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;

    public static CommentDetailResult from(CommentDetailRecord record)
    {
        return CommentDetailResult.builder()
                .postId(record.getPostId())
                .answerId(record.getAnswerId())
                .authorId(record.getAuthorId())
                .authorProfileImage(record.getAuthorProfileImage())
                .authorNickname(record.getAuthorNickname())
                .anonymous(record.isAnonymous())
                .content(record.getContent())
                .build();
    }

    public static List<CommentDetailResult> from(List<CommentDetailRecord> records)
    {
        return records.stream()
                .map(CommentDetailResult::from)
                .toList();
    }
}
