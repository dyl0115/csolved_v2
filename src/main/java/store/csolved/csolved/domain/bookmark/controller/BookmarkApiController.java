package store.csolved.csolved.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.bookmark.service.BookmarkService;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;

@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@RestController
public class BookmarkApiController
{
    private final BookmarkService bookmarkService;

    @LoginRequest
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void save(@LoginUser User user,
                     @PathVariable Long postId)
    {
        bookmarkService.save(user.getId(), postId);
    }

    @LoginRequest
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@LoginUser User user,
                       @PathVariable Long postId)
    {
        bookmarkService.delete(user.getId(), postId);
    }
}
