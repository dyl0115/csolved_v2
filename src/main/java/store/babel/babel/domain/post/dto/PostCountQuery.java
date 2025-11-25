package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.post.controller.dto.PostSearchRequest;

import java.util.Objects;

@Getter
@Builder
public class PostCountQuery
{
    private Long categoryId;
    private String searchType;
    private String searchKeyword;

    public static PostCountQuery from(PostSearchRequest request)
    {
        return PostCountQuery.builder()
                .categoryId(request.getCategoryId())
                .searchType(request.getSearchType())
                .searchKeyword(request.getSearchKeyword())
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
