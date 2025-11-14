package store.babel.babel.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.community.mapper.record.CommunityRecord;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class UserCommunitiesWithPaginationResult
{
    private List<CommunityRecord> posts;
    private Pagination page;

    public static UserCommunitiesWithPaginationResult from(List<CommunityRecord> posts,
                                                           Pagination page)
    {
        return UserCommunitiesWithPaginationResult.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
