package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.global.utils.filter.Filtering;
import store.babel.babel.global.utils.search.Searching;
import store.babel.babel.global.utils.sort.Sorting;

import static store.babel.babel.common.PostType.COMMUNITY;

@Getter
@Builder
public class PostSearchQuery
{
    private Long postType;
    private Long pageNumber;
    private Sorting sort;
    private Filtering filter;
    private Searching search;

    public static PostSearchQuery from(Long pageNumber, Sorting sort, Filtering filter, Searching searching)
    {
        return PostSearchQuery.builder()
                .postType(COMMUNITY.getCode())
                .pageNumber(pageNumber)
                .sort(sort)
                .filter(filter)
                .search(searching)
                .build();
    }
}
