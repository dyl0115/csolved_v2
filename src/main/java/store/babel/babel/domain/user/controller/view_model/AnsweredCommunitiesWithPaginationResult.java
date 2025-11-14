package store.babel.babel.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.community.mapper.record.CommunityRecord;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class AnsweredCommunitiesWithPaginationResult
{
    private List<CommunityRecord> posts;
    private Pagination page;

    public static AnsweredCommunitiesWithPaginationResult from(List<CommunityRecord> posts,
                                                               Pagination page)
    {
        return AnsweredCommunitiesWithPaginationResult.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
