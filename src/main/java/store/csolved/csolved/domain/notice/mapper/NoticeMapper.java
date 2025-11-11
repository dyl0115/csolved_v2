package store.csolved.csolved.domain.notice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;
import store.csolved.csolved.utils.page.Pagination;
import store.csolved.csolved.utils.search.Searching;

import java.util.List;

@Mapper
public interface NoticeMapper
{
    void saveNotice(int postType, Notice notice);

    void updateNotice(Long noticeId, Notice notice);

    Long countNotices(@Param("postType") int postType, @Param("search") Searching search);

    List<Notice> getNotices(int postType,
                            @Param("page") Pagination page,
                            @Param("search") Searching search);

    Notice getNotice(Long noticeId);

    void deleteNotice(Long noticeId);

    boolean hasUserLiked(Long noticeId, Long authorId);

    void increaseLikes(Long noticeId);

    void addUserLike(Long noticeId, Long authorId);

    void increaseView(Long noticeId);
}
