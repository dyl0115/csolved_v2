package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class PostCardWithPage
{
    List<PostCard> postCards;
    Pagination pagination;

    public static PostCardWithPage from(List<PostCard> postCards, Pagination page)
    {
        return PostCardWithPage.builder()
                .postCards(postCards)
                .pagination(page)
                .build();
    }
}
