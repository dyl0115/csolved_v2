package store.postHub.postHub.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.bookmark.service.BookmarkService;
import store.postHub.postHub.domain.user.User;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.global.utils.login.LoginUser;

@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@RestController
public class BookmarkRestController
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
