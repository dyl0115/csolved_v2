package store.csolved.csolved.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.answer.service.command.AnswerCreateCommand;
import store.csolved.csolved.domain.community.service.CommunityService;
import store.csolved.csolved.domain.notice.service.NoticeFacade;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.domain.answer.controller.request.AnswerCreateRequest;
import store.csolved.csolved.domain.answer.service.AnswerService;
import store.csolved.csolved.domain.comment.controller.request.CommentCreateRequest;
import store.csolved.csolved.utils.login.LoginUser;

import static store.csolved.csolved.domain.notice.controller.NoticeController.VIEWS_NOTICE_DETAIL;

@RequiredArgsConstructor
@RestController
public class AnswerApiController
{
    private final NoticeFacade noticeFacade;
    private final AnswerService answerService;
    private final CommunityService communityService;

    @LoginRequest
    @PostMapping("/notice/{postId}/answers")
    public String saveNoticeAnswer(@PathVariable Long postId,
                                   @Valid @ModelAttribute("answerCreateForm") AnswerCreateRequest request,
                                   BindingResult result,
                                   Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("noticeDetails", noticeFacade.getNotice(postId));
            model.addAttribute("commentCreateForm", CommentCreateRequest.empty());
            return VIEWS_NOTICE_DETAIL;
        }
//        answerService.saveAnswer(Answer.from(n));
        return "redirect:/notice/" + postId + "/read";
    }

    @LoginRequest
    @PostMapping("/api/answer")
    public void saveCommunityAnswer(@LoginUser User user,
                                    @Valid @RequestBody AnswerCreateRequest request)
    {
        answerService.saveAnswer(AnswerCreateCommand.from(request));
    }

    @LoginRequest
    @DeleteMapping("/api/answer/{answerId}")
    public void deleteAnswer(@PathVariable Long answerId)
    {
        answerService.delete(answerId);
    }
}