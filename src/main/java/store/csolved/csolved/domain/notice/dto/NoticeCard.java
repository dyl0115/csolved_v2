package store.csolved.csolved.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeCard
{
    private Long id;
    private String title;
    private boolean anonymous;
    private Long authorId;
    private String authorNickname;
    private Long views;
    private Long likes;
    private Long answerCount;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
