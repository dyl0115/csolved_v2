package store.postHub.postHub.domain.notice.service.result;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.domain.notice.mapper.entity.Notice;
import store.postHub.postHub.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class NoticesAndPageResult
{
    private List<Notice> notices;
    private Pagination page;

    public static NoticesAndPageResult from(List<Notice> notices, Pagination page)
    {
        return NoticesAndPageResult.builder()
                .notices(notices)
                .page(page)
                .build();
    }
}
