package store.csolved.csolved.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class CommunitiesAndPageResult
{
    List<Community> posts;
    Pagination page;

    public static CommunitiesAndPageResult from(List<Community> posts, Pagination page)
    {
        return CommunitiesAndPageResult.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
