package store.csolved.csolved.domain.community.mapper.param;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.community.service.command.CommunitySearchCommand;
import store.csolved.csolved.global.utils.filter.Filtering;
import store.csolved.csolved.global.utils.page.Pagination;
import store.csolved.csolved.global.utils.search.Searching;
import store.csolved.csolved.global.utils.sort.Sorting;

import static store.csolved.csolved.common.PostType.COMMUNITY;

@Getter
@Builder
public class CommunityCountParam
{
    private Long postType;
    private String filterType;
    private Long filterValue;
    private String searchType;
    private String searchKeyword;

    public static CommunityCountParam from(CommunitySearchCommand command)
    {
        return CommunityCountParam.builder()
                .postType(COMMUNITY.getCode())
                .filterType(command.getFilter().getFilterType())
                .filterValue(command.getFilter().getFilterValue())
                .searchType(command.getSearch().getSearchType())
                .searchKeyword(command.getSearch().getSearchKeyword())
                .build();
    }
}
