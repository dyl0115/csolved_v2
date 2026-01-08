package store.csolved.csolved.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.post.dto.Post;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class AnsweredCommunitiesWithPaginationResult
{
    private List<Post> posts;
    private Pagination page;

    public static AnsweredCommunitiesWithPaginationResult from(List<Post> posts,
                                                               Pagination page)
    {
        return AnsweredCommunitiesWithPaginationResult.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
