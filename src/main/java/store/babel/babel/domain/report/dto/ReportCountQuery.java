package store.babel.babel.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportCountQuery
{
    private String status;
    private String targetType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String keyword;

    public static ReportCountQuery from(ReportSearchRequest request)
    {
        return ReportCountQuery.builder()
                .status(extractName(request.getStatus()))
                .targetType(extractName(request.getTargetType()))
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .keyword(request.getKeyword())
                .build();
    }

    private static String extractName(Enum<?> enumValue)
    {
        return enumValue != null ? enumValue.name() : null;
    }
}
