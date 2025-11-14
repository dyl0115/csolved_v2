package store.postHub.postHub.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.comment.service.command.CommentCreateCommand;
import store.postHub.postHub.domain.user.User;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.domain.comment.controller.request.CommentCreateRequest;
import store.postHub.postHub.domain.comment.service.CommentService;
import store.postHub.postHub.global.utils.login.LoginUser;

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
