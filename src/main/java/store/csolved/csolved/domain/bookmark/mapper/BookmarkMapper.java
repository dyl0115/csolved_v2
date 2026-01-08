package store.csolved.csolved.domain.bookmark.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookmarkMapper
{
    void saveBookmark(Long userId, Long postId);

    void deleteBookmark(Long userId, Long postId);

    Long countBookmarks(Long userId);

    boolean hasBookmarked(Long userId, Long postId);
}