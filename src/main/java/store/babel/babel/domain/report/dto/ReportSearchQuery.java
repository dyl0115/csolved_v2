package store.babel.babel.domain.report.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportSearchQuery
{
    private String status;
    private String targetType;
    private Long offset;
    private Long size;
    private String sortType = "CREATED_AT";
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String keyword;

    public static ReportSearchQuery from(ReportSearchRequest request)
    {
        return ReportSearchQuery.builder()
                .build();
    }
}
