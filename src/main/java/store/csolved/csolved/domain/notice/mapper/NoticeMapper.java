package store.csolved.csolved.domain.notice.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.notice.dto.*;

import java.util.List;

@Mapper
public interface NoticeMapper
{
    void saveNotice(NoticeCreateCommand command);

    void updateNotice(NoticeUpdateCommand command);

    Long countNotices(NoticeCountQuery query);

    List<NoticeCard> getNoticeCards(NoticeSearchQuery query);

    Notice getNotice(Long noticeId);

    void deleteNotice(Long noticeId);

    boolean hasUserLiked(Long noticeId, Long authorId);

    void increaseLikes(Long noticeId);

    void addUserLike(Long noticeId, Long authorId);

    void increaseView(Long noticeId);
}
