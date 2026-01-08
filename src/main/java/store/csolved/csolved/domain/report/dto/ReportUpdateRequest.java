package store.csolved.csolved.domain.report.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReportUpdateRequest
{
    @NotNull
    private ReportStatus status; // RESOLVED, REJECTED

    @NotNull(message = "관리자 ID는 필수입니다")
    @Positive(message = "관리자 ID는 양수여야 합니다")
    private Long adminId;

    @NotNull(message = "대상 타입은 필수입니다")
    private ReportTargetType targetType;

    @NotNull(message = "대상 ID는 필수입니다")
    @Positive(message = "대상 ID는 양수여야 합니다")
    private Long targetId;
}
