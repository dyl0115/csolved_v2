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
    @PutMapping("/approve")
    public void approveReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.approveReports(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/reject")
    public void rejectReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.rejectReports(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/restore")
    public void rejectApprovedReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.rejectApprovedReports(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/reprocess")
    public void approveRejectedReports(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.approveRejectedReports(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/re-pending")
    public void resetToPending(@Valid @RequestBody ReportUpdateRequest request)
    {
        reportService.resetToPending(ReportUpdateCommand.from(request));
    }

    @LoginRequest
    @GetMapping("/{reportId}")
    public String getDetailReason(@PathVariable("reportId") Long reportId)
    {
        return reportService.getDetailReason(reportId);
    }
}
