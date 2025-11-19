package store.babel.babel.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.report.dto.ReportCreateCommand;
import store.babel.babel.domain.report.dto.ReportCreateRequest;
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
        System.out.println("ReportApiController request " + request.toString());
        reportService.createReport(ReportCreateCommand.from(request));
    }

    @LoginRequest
    @GetMapping("/{reportId}")
    public String getDetailReason(@PathVariable("reportId") Long reportId)
    {
        return reportService.getDetailReason(reportId);
    }
}
