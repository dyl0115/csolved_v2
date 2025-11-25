package store.babel.babel.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.notice.controller.dto.NoticeSearchRequest;

import java.util.Objects;

@Getter
@Builder
public class NoticeCountQuery
{
    private String searchType;
    private String searchKeyword;

    public static NoticeCountQuery from(NoticeSearchRequest request)
    {
        return NoticeCountQuery.builder()
                .searchType(request.getSearchType())
                .searchKeyword(request.getSearchKeyword())
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
