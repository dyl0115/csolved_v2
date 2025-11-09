package store.csolved.csolved.domain.community.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.category.Category;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class CommunityListVM
{
    private Pagination page;
    private List<Category> categories;
    private List<Community> posts;

    public static CommunityListVM from(Pagination page,
                                       List<Category> categories,
                                       List<Community> communities)
    {
        return CommunityListVM.builder()
                .page(page)
                .categories(categories)
                .posts(communities)
                .build();
    }
}
