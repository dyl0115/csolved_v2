package store.babel.babel.domain.report.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ReportSearchRequest
{
    private ReportStatus status;

    private ReportTargetType targetType;

    @Min(0)
    private Long page;

    @Min(0)
    @Max(100)
    private Long size;

    private ReportSortType sortType;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String keyword;
}
