package store.babel.babel.domain.comment.service.result;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.comment.mapper.record.CommentDetailRecord;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentDetailResult
{
    private Long id;
    private Long answerId;
    private Long authorId;
    private String authorProfileImage;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDetailResult from(CommentDetailRecord record)
    {
        return CommentDetailResult.builder()
                .id(record.getId())
                .answerId(record.getAnswerId())
                .authorId(record.getAuthorId())
                .authorProfileImage(record.getAuthorProfileImage())
                .authorNickname(record.getAuthorNickname())
                .anonymous(record.isAnonymous())
                .content(record.getContent())
                .createdAt(record.getCreatedAt())
                .build();
    }

    public static List<CommentDetailResult> from(List<CommentDetailRecord> records)
    {
        return records.stream()
                .map(CommentDetailResult::from)
                .toList();
    }
}
