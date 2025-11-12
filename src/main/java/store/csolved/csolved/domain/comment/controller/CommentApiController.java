package store.csolved.csolved.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.comment.service.command.CommentCreateCommand;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.domain.comment.controller.request.CommentCreateRequest;
import store.csolved.csolved.domain.comment.service.CommentService;
import store.csolved.csolved.global.utils.login.LoginUser;

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
