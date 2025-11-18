package store.babel.babel.domain.report.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportSearchRequest
{
    private ReportStatus status;

    private ReportType targetType;

    @Min(0)
    private Long page;

    @Min(0)
    @Max(100)
    private int size;

    private String sort = "CREATED_AT";

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String keyword;
}
