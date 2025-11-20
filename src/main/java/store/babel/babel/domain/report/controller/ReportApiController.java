package store.babel.babel.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.report.dto.ReportCreateCommand;
import store.babel.babel.domain.report.dto.ReportCreateRequest;
import store.babel.babel.domain.report.dto.ReportUpdateCommand;
import store.babel.babel.domain.report.dto.ReportUpdateRequest;
import store.babel.babel.domain.report.service.ReportService;
import store.babel.babel.global.utils.login.LoginRequest;

@RequestMapping("/api/report")
@RestController
@RequiredArgsConstructor
public class ReportApiController
{
    private final ReportService reportService;

    @LoginRequest
    @PostMapping
    public void createReport(@Valid @RequestBody ReportCreateRequest request)
    {
        reportService.createReport(ReportCreateCommand.from(request));
    }

    @LoginRequest
    @PutMapping
    public void updateReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.updateReports(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/restore")
    public void undoReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.undoReportActions(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/reprocess")
    public void reprocessReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.reprocessReports(ReportUpdateCommand.from(request));
    }


    @LoginRequest
    @GetMapping("/{reportId}")
    public String getDetailReason(@PathVariable("reportId") Long reportId)
    {
        return reportService.getDetailReason(reportId);
    }
}
