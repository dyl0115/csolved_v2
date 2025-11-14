package store.babel.babel.domain.notice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import store.babel.babel.domain.notice.mapper.entity.Notice;
import store.babel.babel.global.utils.page.Pagination;
import store.babel.babel.global.utils.search.Searching;

import java.util.List;

@Mapper
public interface NoticeMapper
{
    void saveNotice(Long postType, Notice notice);

    void updateNotice(Long noticeId, Notice notice);

    Long countNotices(@Param("postType") Long postType, @Param("search") Searching search);

    List<Notice> getNotices(Long postType,
                            @Param("page") Pagination page,
                            @Param("search") Searching search);

    Notice getNotice(Long noticeId);

    void deleteNotice(Long noticeId);

    boolean hasUserLiked(Long noticeId, Long authorId);

    void increaseLikes(Long noticeId);

    void addUserLike(Long noticeId, Long authorId);

    void increaseView(Long noticeId);
}
