package store.csolved.csolved.domain.user.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.bookmark.PostCard;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class BookmarksAndPageVM
{
    private List<PostCard> bookmarks;
    private Pagination page;

    public static BookmarksAndPageVM from(List<PostCard> bookmarks,
                                          Pagination page)
    {
        return BookmarksAndPageVM.builder()
                .bookmarks(bookmarks)
                .page(page)
                .build();
    }
}
