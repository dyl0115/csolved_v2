package store.csolved.csolved.domain.bookmark.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.bookmark.PostCard;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Mapper
public interface BookmarkMapper
{
    void saveBookmark(Long userId, Long postId);

    void deleteBookmark(Long userId, Long postId);

    List<PostCard> getBookmarks(Long userId, Pagination page);

    Long countBookmarks(Long userId);

    boolean hasBookmarked(Long userId, Long postId);
}