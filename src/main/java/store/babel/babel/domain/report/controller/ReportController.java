package store.babel.babel.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.report.dto.ReportCard;
import store.babel.babel.domain.report.dto.ReportCountQuery;
import store.babel.babel.domain.report.dto.ReportSearchQuery;
import store.babel.babel.domain.report.dto.ReportSearchRequest;
import store.babel.babel.domain.report.service.ReportService;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequestMapping("/admin/report")
@RequiredArgsConstructor
@Controller
public class ReportController
{
    private static final String VIEW_REPORTS = "/views/report/list";

    private final ReportService reportService;

    @LoginRequest
    @GetMapping
    public String getReports(@LoginUser User user,
                             @Valid @ModelAttribute ReportSearchRequest request,
                             Model model)
    {
        System.out.println("ReportSearchRequest " + request.toString());
        Pagination pagination = Pagination.from(request.getPage(), reportService.countReports(ReportCountQuery.from(request)), request.getSize());
        System.out.println("Pagination " + pagination);
        List<ReportCard> reports = reportService.getReports(ReportSearchQuery.from(request, pagination));

        model.addAttribute("reports", reports);
        model.addAttribute("page", pagination);
        model.addAttribute("totalCount", reportService.countAll());
        model.addAttribute("pendingCount", reportService.countPending());
        model.addAttribute("reviewingCount", reportService.countReviewing());
        model.addAttribute("resolvedCount", reportService.countResolved());

        return VIEW_REPORTS;
    }


}
