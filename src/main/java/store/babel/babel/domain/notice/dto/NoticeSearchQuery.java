package store.babel.babel.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.global.utils.search.Searching;

import static store.babel.babel.domain.post.dto.PostType.NOTICE;

@Getter
@Builder
public class NoticeSearchQuery
{
    private Long postType;
    private Long pageNumber;
    private String searchType;
    private String searchKeyword;

    public static NoticeSearchQuery from(Long pageNumber, Searching searching)
    {
        return NoticeSearchQuery.builder()
                .postType(NOTICE.getCode())
                .pageNumber(pageNumber)
                .searchType(searching.getSearchType())
                .searchKeyword(searching.getSearchKeyword())
                .build();
    }
}
