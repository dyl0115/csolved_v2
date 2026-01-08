package store.csolved.csolved.domain.report.dto;

public enum ReportStatus
{
    PENDING, // 접수됨
    APPROVED, // 해결됨 (조치완료)
    REJECTED // 반려됨 (부적절한 신고)
}