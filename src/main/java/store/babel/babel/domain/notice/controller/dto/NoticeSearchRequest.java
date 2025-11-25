package store.babel.babel.domain.notice.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import store.babel.babel.domain.post.dto.SearchType;

import java.util.Optional;

@Getter
@Setter
@Builder
public class NoticeSearchRequest
{
    private SearchType searchType;
    private String searchKeyword;
    private Long page;

    public String getSearchType()
    {
        return Optional.ofNullable(searchType)
                .map(Enum::name)
                .orElse(null);
    }

    public Long getPage()
    {
        return Optional.ofNullable(page)
                .orElse(1L);
    }
}
