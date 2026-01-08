package store.csolved.csolved.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.report.dto.*;

import java.util.List;

@Mapper
public interface ReportMapper
{
    void createReport(ReportCreateCommand command);

    List<ReportCard> getReports(ReportSearchQuery query);

    // 해당 게시글에 대한 신고들을 인정합니다.
    void approveReports(ReportUpdateCommand command);

    // 해당 게시글에 대한 신고들을 반려합니다.
    void rejectReports(ReportUpdateCommand command);

    // 신고를 REJECTED -> APPROVED 변경
    void approveRejectedReports(ReportUpdateCommand command);

    // 신고를 APPROVED -> REJECTED 변경
    void rejectApprovedReports(ReportUpdateCommand command);

    // REJECTED/APPROVED 된 신고 -> PENDING 변경
    void resetToPending(ReportUpdateCommand command);

    Long countReports(ReportCountQuery query);

    Long countAll();

    Long countPending();

    Long countRejected();

    Long countApproved();

    String getDetailReason(Long reportId);
}
