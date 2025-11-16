package store.babel.babel.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.comment.dto.CommentCreateCommand;
import store.babel.babel.domain.user.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.domain.comment.controller.dto.CommentCreateRequest;
import store.babel.babel.domain.comment.service.CommentService;
import store.babel.babel.global.utils.login.LoginUser;

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
