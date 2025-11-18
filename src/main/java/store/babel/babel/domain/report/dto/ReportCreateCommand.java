package store.babel.babel.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportCreateCommand
{
    private Long id;
    private Long reporterId;
    private String targetType;
    private Long targetId;
    private String reason;
    private String detailReason;
    private String status;

    public static ReportCreateCommand from(ReportCreateRequest request)
    {
        return ReportCreateCommand.builder()
                .reporterId(request.getReporterId())
                .targetType(request.getTargetType().name())
                .targetId(request.getTargetId())
                .reason(request.getReason().name())
                .detailReason(request.getDetailReason())
                .status(ReportStatus.PENDING.name())
                .build();
    }
}
