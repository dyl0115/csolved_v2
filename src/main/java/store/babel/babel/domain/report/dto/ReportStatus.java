package store.babel.babel.domain.report.dto;

public enum ReportStatus
{
    PENDING, // 접수됨
    REVIEWING, // 검토중
    RESOLVED, // 해결됨 (조치완료)
    REJECTED // 반려됨 (부적절한 신고)
}