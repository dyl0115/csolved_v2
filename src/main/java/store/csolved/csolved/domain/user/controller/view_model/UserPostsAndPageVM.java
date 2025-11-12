package store.csolved.csolved.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.bookmark.PostCard;
import store.csolved.csolved.global.utils.page.Pagination;

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
