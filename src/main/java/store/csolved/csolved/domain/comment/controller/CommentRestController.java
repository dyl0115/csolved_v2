package store.csolved.csolved.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;
import store.csolved.csolved.domain.comment.service.CommentService;
import store.csolved.csolved.domain.user.User;

@RequestMapping("api/comments")
@RequiredArgsConstructor
@RestController
public class CommentRestController
{
    private final CommentService commentService;

    @LoginRequest
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@LoginUser User user,
                       @PathVariable("commentId") Long commentId)
    {
        commentService.delete(user.getId(), commentId);
    }
}
