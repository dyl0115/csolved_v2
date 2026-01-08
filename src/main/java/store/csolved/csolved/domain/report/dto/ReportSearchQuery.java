package store.csolved.csolved.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import store.csolved.csolved.global.utils.page.Pagination;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ReportSearchQuery
{
    private String status;
    private String targetType;
    private Long offset;
    private Long size;
    private String sortType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String keyword;

    public static ReportSearchQuery from(ReportSearchRequest request, Pagination pagination)
    {
        return ReportSearchQuery.builder()
                .status(extractName(request.getStatus()))
                .targetType(extractName(request.getTargetType()))
                .offset(pagination.getOffset())
                .size(pagination.getSize())
                .sortType(extractName(request.getSortType()))
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
