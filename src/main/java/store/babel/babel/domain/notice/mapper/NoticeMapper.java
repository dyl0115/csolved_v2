package store.babel.babel.domain.notice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import store.babel.babel.domain.notice.dto.*;
import store.babel.babel.domain.notice.service.NoticeService;
import store.babel.babel.global.utils.page.Pagination;
import store.babel.babel.global.utils.search.Searching;

import java.util.List;

@Mapper
public interface NoticeMapper
{
    void saveNotice(NoticeCreateCommand command);

    void updateNotice(NoticeUpdateCommand command);

    Long countNotices(NoticeSearchQuery query);

    List<NoticeCard> getNoticeCards(NoticeSearchQuery query, Pagination pagination);

    Notice getNotice(Long noticeId);

    void deleteNotice(Long noticeId);

    boolean hasUserLiked(Long noticeId, Long authorId);

    void increaseLikes(Long noticeId);

    void addUserLike(Long noticeId, Long authorId);

    void increaseView(Long noticeId);
}
