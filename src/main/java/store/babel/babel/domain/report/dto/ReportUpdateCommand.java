package store.babel.babel.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportUpdateCommand
{
    private ReportStatus status;
    private Long adminId;
    private ReportTargetType targetType;
    private Long targetId;

    public static ReportUpdateCommand from(ReportUpdateRequest request)
    {
        return ReportUpdateCommand.builder()
                .status(request.getStatus())
                .adminId(request.getAdminId())
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .build();
    }
}
