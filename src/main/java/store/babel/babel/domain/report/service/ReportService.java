package store.babel.babel.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.report.dto.ReportCard;
import store.babel.babel.domain.report.dto.ReportCreateCommand;
import store.babel.babel.domain.report.dto.ReportSearchQuery;
import store.babel.babel.domain.report.mapper.ReportMapper;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService
{
    private final ReportMapper reportMapper;

    @Transactional
    public void createReport(ReportCreateCommand command)
    {
        reportMapper.createReport(command);
    }

    public List<ReportCard> getReports(ReportSearchQuery query, Pagination pagination)
    {
        return null;
    }

    @Transactional
    public void deleteReport(Long reportId)
    {

    }

    public Long countReports(ReportSearchQuery query)
    {
        return reportMapper.countReports(query);
    }
}
