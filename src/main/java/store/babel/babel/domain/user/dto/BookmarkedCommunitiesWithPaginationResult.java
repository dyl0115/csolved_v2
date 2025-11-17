package store.babel.babel.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.post.dto.Post;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class BookmarkedCommunitiesWithPaginationResult
{
    private List<Post> bookmarks;
    private Pagination page;

    public static BookmarkedCommunitiesWithPaginationResult from(List<Post> bookmarks,
                                                                 Pagination page)
    {
        return BookmarkedCommunitiesWithPaginationResult.builder()
                .bookmarks(bookmarks)
                .page(page)
                .build();
    }
}
