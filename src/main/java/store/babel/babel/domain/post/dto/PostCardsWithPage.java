package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class PostCardsWithPage
{
    List<PostCard> postCards;
    Pagination pagination;

    public static PostCardsWithPage from(List<PostCard> postCards, Pagination pagination)
    {
        return PostCardsWithPage.builder()
                .postCards(postCards)
                .pagination(pagination)
                .build();
    }
}
