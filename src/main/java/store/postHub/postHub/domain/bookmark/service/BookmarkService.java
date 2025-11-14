package store.postHub.postHub.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.postHub.postHub.domain.bookmark.PostCard;
import store.postHub.postHub.domain.bookmark.mapper.BookmarkMapper;
import store.postHub.postHub.global.utils.page.Pagination;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkService
{
    private final BookmarkMapper bookmarkMapper;

    @Transactional
    public void save(Long userId, Long postId)
    {
        bookmarkMapper.saveBookmark(userId, postId);
    }

    @Transactional
    public void delete(Long userId, Long postId)
    {
        bookmarkMapper.deleteBookmark(userId, postId);
    }

    public List<PostCard> getBookmarks(Long userId, Pagination page)
    {
        return bookmarkMapper.getBookmarks(userId, page);
    }
}
