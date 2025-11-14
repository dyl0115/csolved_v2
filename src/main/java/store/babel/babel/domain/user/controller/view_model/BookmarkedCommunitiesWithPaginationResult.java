package store.babel.babel.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.community.mapper.record.CommunityRecord;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class BookmarkedCommunitiesWithPaginationResult
{
    private List<CommunityRecord> bookmarks;
    private Pagination page;

    public static BookmarkedCommunitiesWithPaginationResult from(List<CommunityRecord> bookmarks,
                                                                 Pagination page)
    {
        return BookmarkedCommunitiesWithPaginationResult.builder()
                .bookmarks(bookmarks)
                .page(page)
                .build();
    }
}
