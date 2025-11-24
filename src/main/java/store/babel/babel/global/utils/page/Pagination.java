package store.babel.babel.global.utils.page;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Pagination
{
    private static final Long DEFAULT_RECORDS_PER_PAGE = 12L;

    private final Long recordsPerPage;
    private final Long currentPage;
    private final Long totalPages;
    private final Long offset;
    private final Long size;

    public static Pagination from(Long requestPage,
                                  Long totalRecords)
    {
        return from(requestPage, totalRecords, DEFAULT_RECORDS_PER_PAGE);
    }

    public static Pagination from(Long requestPage,
                                  Long totalRecords,
                                  Long recordsPerPage)
    {
        Long totalPages = createTotalPages(totalRecords, recordsPerPage);
        Long currentPage = createCurrentPage(requestPage, totalPages);
        Long offset = createOffset(currentPage, recordsPerPage);

        return Pagination.builder()
                .recordsPerPage(recordsPerPage)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .offset(offset)
                .size(recordsPerPage)
                .build();
    }

    private static Long createTotalPages(Long totalRecords,
                                         Long recordsPerPage)
    {
        return Math.max((totalRecords + recordsPerPage - 1) / recordsPerPage, 1);
    }

    private static Long createCurrentPage(Long requestPage,
                                          Long totalPage)
    {
        return Math.min(requestPage, totalPage);
    }

    private static Long createOffset(Long currentPage,
                                     Long recordsPerPage)
    {
        return (currentPage - 1) * recordsPerPage;
    }
}
