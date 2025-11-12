package store.csolved.csolved.domain.notice.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Getter
@Builder
public class NoticeListVM
{
    private Pagination page;
    private List<Notice> posts;

    public static NoticeListVM from(Pagination page,
                                    List<Notice> posts)
    {
        return NoticeListVM.builder()
                .page(page)
                .posts(posts)
                .build();
    }
}
