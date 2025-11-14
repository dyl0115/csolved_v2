package store.babel.babel.domain.community.service.command;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.global.utils.filter.Filtering;
import store.babel.babel.global.utils.search.Searching;
import store.babel.babel.global.utils.sort.Sorting;

import static store.babel.babel.common.PostType.COMMUNITY;

@Getter
@Builder
public class CommunitySearchCommand
{
    private Long postType;
    private Long requestPageNumber;
    private Sorting sort;
    private Filtering filter;
    private Searching search;

    public static CommunitySearchCommand from(Long pageNumber, Sorting sort, Filtering filter, Searching searching)
    {
        return CommunitySearchCommand.builder()
                .postType(COMMUNITY.getCode())
                .requestPageNumber(pageNumber)
                .sort(sort)
                .filter(filter)
                .search(searching)
                .build();
    }
}
