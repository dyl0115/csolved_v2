package store.babel.babel.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.post.dto.PostCard;
import store.babel.babel.domain.report.dto.*;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Mapper
public interface ReportMapper
{
    void createReport(ReportCreateCommand command);

    List<ReportCard> getReports(ReportSearchQuery query);

    // 해당 게시글에 대한 신고들을 인정합니다.
    void resolveReports(ReportUpdateCommand command);

    // 해당 게시글에 대한 신고들을 반려합니다.
    void rejectReports(ReportUpdateCommand command);

    // 해당 게시글에 대한 신고들을 재인정합니다.
    void reprocessReports(ReportUpdateCommand command);

    // 해당 게시글에 대한 신고들을 재반려합니다.
    void undoReportActions(ReportUpdateCommand command);

    Long countReports(ReportCountQuery query);

    Long countAll();

    Long countPending();

    Long countRejected();

    Long countResolved();

    String getDetailReason(Long reportId);
}
