package store.postHub.postHub.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.domain.bookmark.PostCard;
import store.postHub.postHub.global.utils.page.Pagination;

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
