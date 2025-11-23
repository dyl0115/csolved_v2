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


    //TODO: 댓글, 대댓글, 게시글 테이블에 deleted_by를 추가하고, Enum으로 관리할 것.
//관리자에 의해 삭제되었는지, 스스로 삭제했는지에 대한 구분이 필요함.
//스스로 삭제했다면 -> 운영자도 조회 불가. - 이미 삭제된 글입니다. (not found 예외 던지기)
//만약 운영자가 삭제했다면 -> 운영자는 조회 가능.
//신고 처리 시: 본인 삭제된 글은 복구 불가, 신고 반려만 가능하도록 처리.

    //신고 처리 상태:
//- 대기중(PENDING): 아직 처리 안 됨
//- 삭제 조치(DELETED): 운영자가 삭제함
//- 반려(REJECTED): 문제없다고 판단
//- 자동 종료(AUTO_CLOSED): 작성자가 스스로 삭제해서 처리 불필요

    @LoginRequest
    @GetMapping
    public String getReports(@Valid @ModelAttribute ReportSearchRequest request,
                             Model model)
    {
        Pagination pagination = Pagination.from(request.getPage(), reportService.countReports(ReportCountQuery.from(request)), request.getSize());
        List<ReportCard> reports = reportService.getReports(ReportSearchQuery.from(request, pagination));

        model.addAttribute("reports", reports);
        model.addAttribute("page", pagination);
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
