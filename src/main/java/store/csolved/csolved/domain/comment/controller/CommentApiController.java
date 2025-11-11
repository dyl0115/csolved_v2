package store.csolved.csolved.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.comment.service.command.CommentCreateCommand;
import store.csolved.csolved.domain.notice.service.NoticeFacade;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.domain.answer.controller.request.AnswerCreateRequest;
import store.csolved.csolved.domain.comment.controller.request.CommentCreateRequest;
import store.csolved.csolved.domain.comment.service.CommentService;
//import store.csolved.csolved.domain.code_review.controller.CodeReviewController;
//import store.csolved.csolved.domain.question.controller.QuestionController;
//import store.csolved.csolved.domain.code_review.service.CodeReviewFacade;
//import store.csolved.csolved.domain.question.service.QuestionFacade;

import static store.csolved.csolved.domain.notice.controller.NoticeController.*;

@RequiredArgsConstructor
@RestController
public class CommentApiController
{
    private final CommentService commentService;
    private final NoticeFacade noticeFacade;

    // TODO: 여기 싹 수정해야함
    @LoginRequest
    @PostMapping("/notice/{postId}/answers/{answerId}/comment")
    public String saveNoticeComment(@PathVariable("postId") Long postId,
                                    @Valid @ModelAttribute("commentCreateForm") CommentCreateRequest form,
                                    BindingResult result,
                                    Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("noticeDetails", noticeFacade.viewNotice(postId));
            model.addAttribute("answerCreateForm", AnswerCreateRequest.empty());
            model.addAttribute("commentCreateFrom", form);
            return VIEWS_NOTICE_DETAIL;
        }
//        commentService.saveComment(form.toComment());
        return "redirect:/notice/" + postId + "/read";
    }

    @LoginRequest
    @PostMapping("/api/comment")
    public void saveCommunityComment(@Valid @RequestBody CommentCreateRequest request)
    {
        System.out.println("오긴하니?");
        commentService.saveComment(CommentCreateCommand.from(request));
    }
}
