package store.babel.babel.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.babel.babel.domain.bookmark.PostCard;
import store.babel.babel.domain.bookmark.mapper.BookmarkMapper;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.domain.user.controller.view_model.BookmarksAndPageVM;
import store.babel.babel.domain.user.controller.view_model.RepliedPostsAndPageVM;
import store.babel.babel.domain.user.controller.view_model.UserPostsAndPageVM;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserActivityFacade
{
    private final BookmarkMapper bookmarkMapper;
    private final PostService postService;

    public BookmarksAndPageVM getBookmarksAndPage(Long userId,
                                                  Long pageNumber)
    {
        // DB에서 북마크 개수를 가져옴.
        Long total = bookmarkMapper.countBookmarks(userId);

        // 사용자가 요청한 페이지 번호, 북마크 개수를 사용하여 페이지 정보를 생성
        Pagination bookmarksPage = Pagination.from(pageNumber, total);

        // 페이지 정보를 사용하여 DB에서 필요한 북마크만 조회.
        List<PostCard> bookmarks = bookmarkMapper.getBookmarks(userId, bookmarksPage);

        return BookmarksAndPageVM.from(bookmarks, bookmarksPage);
    }

    public RepliedPostsAndPageVM getRepliedPostsAndPage(Long userId,
                                                        Long pageNumber)
    {
        // DB에서 회원의 댓글과 대댓글과 관련된 게시글들의 수를 가져옴.
        Long total = postService.countRepliedPosts(userId);

        // 가져온 게시글들의 개수를 사용하여 페이지 정보를 생성
        Pagination page = Pagination.from(pageNumber, total);

        // 페이지 정보를 사용하여 회원의 댓글과 대댓글과 관련된 게시글들을 조회
        List<PostCard> posts = postService.getRepliedPosts(userId, page);

        return RepliedPostsAndPageVM.from(posts, page);
    }

    public UserPostsAndPageVM getUserPostsAndPage(Long userId,
                                                  Long pageNumber)
    {
        // DB에서 회원이 작성한 게시글의 수를 가져옴.
        Long total = postService.countUserPosts(userId);

        // 가져온 게시글들의 개수를 사용하여 페이지 정보를 생성
        Pagination page = Pagination.from(pageNumber, total);

        // 페이지 정보를 사용하여 회원의 게시글들을 조회
        List<PostCard> posts = postService.getUserPosts(userId, page);

        return UserPostsAndPageVM.from(posts, page);
    }
}
