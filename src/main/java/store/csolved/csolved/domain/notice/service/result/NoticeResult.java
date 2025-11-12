package store.csolved.csolved.domain.notice.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;

@Getter
@Builder
public class NoticeResult
{
    private Long id;
    private String postType;
    private String title;
    private boolean anonymous;
    private Long authorId;
    private String authorNickname;
    private String content;
    private Long views;
    private Long likes;
    private Long answerCount;

    public static NoticeResult from(Notice notice)
    {
        return NoticeResult.builder()
                .id(notice.getId())
                .postType(notice.getPostType())
                .title(notice.getTitle())
                .anonymous(notice.isAnonymous())
                .authorId(notice.getAuthorId())
                .authorNickname(notice.getAuthorNickname())
                .content(notice.getContent())
                .views(notice.getViews())
                .likes(notice.getLikes())
                .answerCount(notice.getAnswerCount())
                .build();
    }
}
