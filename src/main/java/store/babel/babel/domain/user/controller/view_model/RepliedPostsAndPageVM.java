package store.babel.babel.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.bookmark.PostCard;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class RepliedPostsAndPageVM
{
    private List<PostCard> posts;
    private Pagination page;

    public static RepliedPostsAndPageVM from(List<PostCard> posts,
                                             Pagination page)
    {
        return RepliedPostsAndPageVM.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
