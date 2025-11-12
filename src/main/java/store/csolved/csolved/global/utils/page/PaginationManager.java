package store.csolved.csolved.global.utils.page;

import org.springframework.stereotype.Component;

@Component
public class PaginationManager
{
    public Pagination createPagination(Long pageNumber, Long totalRecords)
    {
        return Pagination.create(pageNumber, totalRecords);
    }
}
