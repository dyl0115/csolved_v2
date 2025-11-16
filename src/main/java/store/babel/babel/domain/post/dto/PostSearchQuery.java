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
    private String sortType;
    private String filterType;
    private Long filterValue;
    private String searchType;
    private String searchKeyword;

    public static PostSearchQuery from(Long pageNumber, Sorting sort, Filtering filter, Searching searching)
    {
        return PostSearchQuery.builder()
                .postType(COMMUNITY.getCode())
                .pageNumber(pageNumber)
                .sortType(sort.name())
                .filterType(filter.getFilterType())
                .filterValue(filter.getFilterValue())
                .searchType(searching.getSearchType())
                .searchKeyword(searching.getSearchKeyword())
                .build();
    }
}
