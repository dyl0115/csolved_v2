package store.csolved.csolved.global.utils.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Data
public class Pagination
{
    private final static Long POSTS_PER_PAGE = 7L;

    private final Long currentPage;
    private final Long totalPage;
    private final Long offset;
    private final Long size;

    public static Pagination create(Long requestPage,
                                    Long totalRecordsCount)
    {
        Long totalPage = createTotalPage(totalRecordsCount);
        Long currentPage = createCurrentPage(requestPage, totalPage);
        Long offset = createOffset(currentPage);

        return new Pagination(
                currentPage,
                totalPage,
                offset,
                POSTS_PER_PAGE);
    }

    private static Long createTotalPage(Long totalRecordsCount)
    {
        return Math.max((totalRecordsCount + POSTS_PER_PAGE - 1) / POSTS_PER_PAGE, 1);
    }

    private static Long createCurrentPage(Long requestPage,
                                          Long totalPage)
    {
        return Math.min(requestPage, totalPage);
    }

    private static Long createOffset(Long currentPage)
    {
        return (currentPage - 1) * POSTS_PER_PAGE;
    }
}
