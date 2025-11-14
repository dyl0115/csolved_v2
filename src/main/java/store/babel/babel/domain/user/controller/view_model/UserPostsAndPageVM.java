package store.babel.babel.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.bookmark.PostCard;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class UserPostsAndPageVM
{
    private List<PostCard> posts;
    private Pagination page;

    public static UserPostsAndPageVM from(List<PostCard> posts,
                                          Pagination page)
    {
        return UserPostsAndPageVM.builder()
                .posts(posts)
                .page(page)
                .build();
    }
}
