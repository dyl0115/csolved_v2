package store.csolved.csolved.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.notice.controller.dto.NoticeSearchRequest;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.Objects;

@Getter
@Builder
public class NoticeSearchQuery
{
    private String searchType;
    private String searchKeyword;
    private Long limit;
    private Long offset;

    public static NoticeSearchQuery from(NoticeSearchRequest request, Pagination pagination)
    {
        return NoticeSearchQuery.builder()
                .searchType(request.getSearchType())
                .searchKeyword(request.getSearchKeyword())
                .limit(pagination.getSize())
                .offset(pagination.getOffset())
                .build();
    }
    
    public boolean isTitleKeywordProvided()
    {
        return Objects.equals(searchType, "TITLE")
                && searchKeyword != null
                && !searchKeyword.isBlank();
    }

    public boolean isAuthorKeywordProvided()
    {
        return Objects.equals(searchType, "AUTHOR")
                && searchKeyword != null
                && !searchKeyword.isBlank();
    }
}
