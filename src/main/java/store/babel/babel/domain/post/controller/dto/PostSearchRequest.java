package store.babel.babel.domain.post.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearchRequest
{
    Long pageNumber;
    String sortType;
    String filterType;
    String filterValue;
    String searchType;
    String searchValue;
}
