package store.babel.babel.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import store.babel.babel.domain.report.dto.ReportCreateCommand;
import store.babel.babel.domain.report.dto.ReportCreateRequest;
import store.babel.babel.domain.report.service.ReportService;

@RequestMapping("/api/report")
@Controller
@RequiredArgsConstructor
public class ReportApiController
{
    private final ReportService reportService;

    @PostMapping
    public void createReport(@Valid @RequestBody ReportCreateRequest request)
    {
        reportService.createReport(ReportCreateCommand.from(request));
    }

    @DeleteMapping
    public void deleteReport(Long reportId)
    {
        reportService.deleteReport(reportId);
    }
}
