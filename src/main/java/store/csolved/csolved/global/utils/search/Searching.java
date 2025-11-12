package store.csolved.csolved.global.utils.search;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Searching
{
    private String searchType;
    private String keyword;

    public static Searching create(String searchType,
                                   String keyword)
    {
        return Searching.builder()
                .searchType(searchType)
                .keyword(keyword)
                .build();
    }
}
