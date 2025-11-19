package store.babel.babel.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.report.dto.ReportCard;
import store.babel.babel.domain.report.dto.ReportCountQuery;
import store.babel.babel.domain.report.dto.ReportCreateCommand;
import store.babel.babel.domain.report.dto.ReportSearchQuery;
import store.babel.babel.domain.report.mapper.ReportMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService
{
    private final ReportMapper reportMapper;

    @Transactional
    public void createReport(ReportCreateCommand command)
    {
        System.out.println("ReportService command " + command.toString());
        reportMapper.createReport(command);
    }

    public List<ReportCard> getReports(ReportSearchQuery query)
    {
        return reportMapper.getReports(query);
    }

    public Long countAll()
    {
        return reportMapper.countAll();
    }

    public Long countPending()
    {
        return reportMapper.countPending();
    }

    public Long countReviewing()
    {
        return reportMapper.countReviewing();
    }

    public Long countResolved()
    {
        return reportMapper.countResolved();
    }

    public Long countReports(ReportCountQuery query)
    {
        return reportMapper.countReports(query);
    }

    public String getDetailReason(Long reportId)
    {
        return reportMapper.getDetailReason(reportId);
    }
}
