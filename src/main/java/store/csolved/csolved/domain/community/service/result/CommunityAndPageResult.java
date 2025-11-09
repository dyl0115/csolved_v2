package store.csolved.csolved.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class CommunityAndPageResult
{
    List<Community> communities;
    Pagination page;

    public static CommunityAndPageResult from(List<Community> communities, Pagination page)
    {
        return CommunityAndPageResult.builder()
                .communities(communities)
                .page(page)
                .build();
    }
}
