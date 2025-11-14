package store.babel.babel.domain.community.mapper.param;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.community.service.command.CommunitySearchCommand;
import store.babel.babel.global.utils.page.Pagination;

import static store.babel.babel.common.PostType.COMMUNITY;

@Getter
@Builder
public class CommunitySearchParam
{
    private Long postType;
    private Long pageOffset;
    private Long pageSize;
    private String sortType;
    private String filterType;
    private Long filterValue;
    private String searchType;
    private String searchKeyword;

    public static CommunitySearchParam from(CommunitySearchCommand command, Pagination pagination)
    {
        return CommunitySearchParam.builder()
                .postType(COMMUNITY.getCode())
                .pageOffset(pagination.getOffset())
                .pageSize(pagination.getSize())
                .sortType(command.getSort().name())
                .filterType(command.getFilter().getFilterType())
                .filterValue(command.getFilter().getFilterValue())
                .searchType(command.getSearch().getSearchType())
                .searchKeyword(command.getSearch().getSearchKeyword())
                .build();
    }
}
