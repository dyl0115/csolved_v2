package store.babel.babel.domain.report.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.answer.dto.Answer;
import store.babel.babel.domain.answer.service.AnswerService;
import store.babel.babel.domain.comment.dto.Comment;
import store.babel.babel.domain.comment.service.CommentService;
import store.babel.babel.domain.report.dto.ReportCard;
import store.babel.babel.domain.report.dto.ReportCountQuery;
import store.babel.babel.domain.report.dto.ReportSearchQuery;
import store.babel.babel.domain.report.dto.ReportSearchRequest;
import store.babel.babel.domain.report.service.ReportService;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequestMapping("/admin/report")
@RequiredArgsConstructor
@Controller
public class ReportController
{
    private static final String VIEW_REPORTS = "/views/report/list";
    private static final String REPORTED_COMMENT_MODAL = "/fragments/report-modal :: reported-commend-modal";
    private static final String REPORTED_ANSWER_MODAL = "/fragments/report-modal :: reported-answer-modal";

    private final ReportService reportService;
    private final AnswerService answerService;
    private final CommentService commentService;

    @LoginRequest
    @GetMapping
    public String getReports(@Valid @ModelAttribute ReportSearchRequest request,
                             Model model)
    {
        Pagination pagination = Pagination.from(request.getPage(), reportService.countReports(ReportCountQuery.from(request)), request.getSize());
        List<ReportCard> reports = reportService.getReports(ReportSearchQuery.from(request, pagination));
        model.addAttribute("reports", reports);
        model.addAttribute("pagination", pagination);
        model.addAttribute("totalCount", reportService.countAll());
        model.addAttribute("pendingCount", reportService.countPending());
        model.addAttribute("rejectedCount", reportService.countRejected());
        model.addAttribute("approvedCount", reportService.countApproved());

        return VIEW_REPORTS;
    }

    @LoginRequest
    @GetMapping("/answer/{answerId}")
    public String getReportedAnswer(@PathVariable("answerId") Long answerId,
                                    Model model)
    {
        Answer answer = answerService.getAnswerForAdmin(answerId);
        model.addAttribute("answer", answer);
        return REPORTED_ANSWER_MODAL;
    }

    @LoginRequest
    @GetMapping("/comment/{commentId}")
    public String getReportedComment(@PathVariable("commentId") Long commentId,
                                     Model model)
    {
        Comment comment = commentService.getCommentForAdmin(commentId);
        model.addAttribute("comment", comment);
        return REPORTED_COMMENT_MODAL;
    }
}
