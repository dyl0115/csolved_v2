package store.babel.babel.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.post.dto.Post;
import store.babel.babel.global.utils.page.Pagination;

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
