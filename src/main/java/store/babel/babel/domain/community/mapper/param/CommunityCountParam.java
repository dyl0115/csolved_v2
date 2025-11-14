package store.babel.babel.domain.community.mapper.param;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.community.service.command.CommunitySearchCommand;

import static store.babel.babel.common.PostType.COMMUNITY;

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
