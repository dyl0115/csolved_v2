package store.csolved.csolved.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.post.dto.Post;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class UserCommunitiesWithPaginationResult
{
    private List<Post> posts;
    private Pagination page;

    public static UserCommunitiesWithPaginationResult from(List<Post> posts,
                                                           Pagination page)
    {
        return UserCommunitiesWithPaginationResult.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
