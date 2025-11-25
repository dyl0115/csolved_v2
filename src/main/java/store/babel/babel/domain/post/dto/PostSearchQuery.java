package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import store.babel.babel.domain.post.controller.dto.PostSearchRequest;
import store.babel.babel.global.utils.page.Pagination;

import java.util.Objects;

@Getter
@Builder
@ToString
public class PostSearchQuery
{
    private Long categoryId;
    private String searchType;
    private String searchKeyword;
    private String sortType;
    private Long limit;
    private Long offset;

    public static PostSearchQuery from(PostSearchRequest request, Pagination pagination)
    {
        return PostSearchQuery.builder()
                .categoryId(request.getCategoryId())
                .searchType(request.getSearchType())
                .searchKeyword(request.getSearchKeyword())
                .sortType(request.getSortType())
                .limit(pagination.getSize())
                .offset(pagination.getOffset())
                .build();
    }

    public boolean isCategoryProvided()
    {
        return categoryId != null;
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
