package store.babel.babel.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.babel.babel.domain.bookmark.mapper.BookmarkMapper;
import store.babel.babel.domain.community.mapper.CommunityMapper;
import store.babel.babel.domain.community.mapper.record.CommunityRecord;
import store.babel.babel.domain.user.controller.view_model.BookmarkedCommunitiesWithPaginationResult;
import store.babel.babel.domain.user.controller.view_model.AnsweredCommunitiesWithPaginationResult;
import store.babel.babel.domain.user.controller.view_model.UserCommunitiesWithPaginationResult;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserActivityService
{
    private final BookmarkMapper bookmarkMapper;
    private final CommunityMapper communityMapper;

    public BookmarkedCommunitiesWithPaginationResult getBookmarkedCommunitiesWithPagination(Long userId,
                                                                                            Long pageNumber)
    {
        // DB에서 북마크 개수를 가져옴.
        Long total = bookmarkMapper.countBookmarks(userId);

        // 사용자가 요청한 페이지 번호, 북마크 개수를 사용하여 페이지 정보를 생성
        Pagination bookmarksPage = Pagination.from(pageNumber, total);

        // 페이지 정보를 사용하여 DB에서 필요한 북마크만 조회.
        List<CommunityRecord> bookmarks = communityMapper.getBookmarkedCommunities(userId, bookmarksPage);


        return BookmarkedCommunitiesWithPaginationResult.from(bookmarks, bookmarksPage);
    }

    public AnsweredCommunitiesWithPaginationResult getAnsweredCommunitiesWithPagination(Long userId,
                                                                                        Long pageNumber)
    {
        // DB에서 회원의 댓글과 대댓글과 관련된 게시글들의 수를 가져옴.
        Long total = communityMapper.countAnsweredCommunities(userId);

        // 가져온 게시글들의 개수를 사용하여 페이지 정보를 생성
        Pagination page = Pagination.from(pageNumber, total);

        // 페이지 정보를 사용하여 회원의 댓글과 대댓글과 관련된 게시글들을 조회
        List<CommunityRecord> posts = communityMapper.getAnsweredCommunities(userId, page);

        return AnsweredCommunitiesWithPaginationResult.from(posts, page);
    }

    public UserCommunitiesWithPaginationResult getUserPostsAndPage(Long userId,
                                                                   Long pageNumber)
    {
        // DB에서 회원이 작성한 게시글의 수를 가져옴.
        Long total = communityMapper.countUserCommunities(userId);

        // 가져온 게시글들의 개수를 사용하여 페이지 정보를 생성
        Pagination page = Pagination.from(pageNumber, total);

        // 페이지 정보를 사용하여 회원의 게시글들을 조회
        List<CommunityRecord> posts = communityMapper.getUserCommunities(userId, page);

        return UserCommunitiesWithPaginationResult.from(posts, page);
    }
}
