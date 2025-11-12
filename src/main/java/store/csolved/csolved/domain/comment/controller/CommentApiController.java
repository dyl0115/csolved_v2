package store.csolved.csolved.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.comment.service.command.CommentCreateCommand;
import store.csolved.csolved.domain.notice.service.NoticeFacade;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.domain.answer.controller.request.AnswerCreateRequest;
import store.csolved.csolved.domain.comment.controller.request.CommentCreateRequest;
import store.csolved.csolved.domain.comment.service.CommentService;
import store.csolved.csolved.utils.login.LoginUser;
//import store.csolved.csolved.domain.code_review.controller.CodeReviewController;
//import store.csolved.csolved.domain.question.controller.QuestionController;
//import store.csolved.csolved.domain.code_review.service.CodeReviewFacade;
//import store.csolved.csolved.domain.question.service.QuestionFacade;

import static store.csolved.csolved.domain.notice.controller.NoticeController.*;

@RequiredArgsConstructor
@RequestMapping("/api/comment")
@RestController
public class CommentApiController
{
    private final CommentService commentService;

    @LoginRequest
    @PostMapping
    public void saveComment(@Valid @RequestBody CommentCreateRequest request)
    {
        commentService.saveComment(CommentCreateCommand.from(request));
    }

    @LoginRequest
    @DeleteMapping("{commentId}")
    public void deleteComment(@LoginUser User user,
                              @PathVariable Long commentId)
    {
        commentService.delete(user.getId(), commentId);
    }
}
