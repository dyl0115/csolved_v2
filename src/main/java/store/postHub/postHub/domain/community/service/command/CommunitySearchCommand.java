package store.postHub.postHub.domain.community.service.command;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.global.utils.filter.Filtering;
import store.postHub.postHub.global.utils.search.Searching;
import store.postHub.postHub.global.utils.sort.Sorting;

import static store.postHub.postHub.common.PostType.COMMUNITY;

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
