package store.babel.babel.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Notice
{
    private Long id;
    private String title;
    private Long authorId;
    private String authorNickname;
    private boolean anonymous;
    private String content;
    private Long views;
    private Long likes;
    private Long answerCount;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
