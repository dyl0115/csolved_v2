package store.babel.babel.domain.post.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import store.babel.babel.domain.post.dto.SearchType;
import store.babel.babel.domain.post.dto.SortType;

import java.util.Optional;

@Getter
@Setter
@Builder
public class PostSearchRequest
{
    private Long categoryId;
    private SearchType searchType;
    private String searchKeyword;
    private SortType sortType;
    private Long page;

    public String getSearchType()
    {
        return Optional.ofNullable(searchType)
                .map(Enum::name)
                .orElse(null);
    }

    public String getSortType()
    {
        return Optional.ofNullable(sortType)
                .map(Enum::name)
                .orElse(null);
    }

    public Long getPage()
    {
        return Optional.ofNullable(page)
                .orElse(1L);
    }
}
