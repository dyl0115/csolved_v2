package store.babel.babel.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.bookmark.mapper.BookmarkMapper;


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
}
