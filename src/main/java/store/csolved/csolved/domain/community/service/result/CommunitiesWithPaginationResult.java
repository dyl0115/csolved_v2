package store.csolved.csolved.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.community.mapper.record.CommunityRecord;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class CommunitiesWithPaginationResult
{
    List<CommunityRecord> posts;
    Pagination page;

    public static CommunitiesWithPaginationResult from(List<CommunityRecord> posts, Pagination page)
    {
        return CommunitiesWithPaginationResult.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
