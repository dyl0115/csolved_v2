package store.babel.babel.domain.report.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@ToString
public class ReportCreateRequest
{
    @NotNull(message = "신고자 ID는 필수입니다")
    @Positive(message = "신고자 ID는 양수여야 합니다")
    private Long reporterId;

    @NotNull(message = "신고 대상 타입은 필수입니다")
    private ReportTargetType targetType;

    @NotNull(message = "신고 대상 ID는 필수입니다")
    @Positive(message = "신고 대상 ID는 양수여야 합니다")
    private Long targetId;

    @NotNull(message = "신고 사유는 필수입니다.")
    private ReportReason reason;

    @Size(max = 500, message = "추가 설명은 500자 이하로 작성해주세요")
    private String detailReason;
}