package store.babel.babel.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.post.dto.PostCard;
import store.babel.babel.domain.report.dto.ReportCard;
import store.babel.babel.domain.report.dto.ReportCreateCommand;
import store.babel.babel.domain.report.dto.ReportSearchQuery;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Mapper
public interface ReportMapper
{
    void createReport(ReportCreateCommand command);

    List<ReportCard> getReports(ReportSearchQuery query, Pagination pagination);

    Long countReports(ReportSearchQuery query);

    Long countAll();

    Long countPending();

    Long countReviewing();

    Long countResolved();

    String getDetailReason(Long reportId);
}
