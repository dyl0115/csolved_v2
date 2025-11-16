package store.babel.babel.domain.notice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.notice.controller.dto.NoticeCreateRequest;
import store.babel.babel.domain.notice.controller.dto.NoticeUpdateRequest;
import store.babel.babel.domain.notice.service.NoticeService;
import store.babel.babel.domain.notice.dto.NoticeCreateCommand;
import store.babel.babel.domain.notice.dto.NoticeUpdateCommand;
import store.babel.babel.domain.user.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

@RequestMapping("/api/notice")
@RequiredArgsConstructor
@RestController
public class NoticeApiController
{
    private final NoticeService noticeService;

    @LoginRequest
    @PostMapping
    public void saveNotice(@Valid @RequestBody NoticeCreateRequest request)
    {
        noticeService.saveNotice(NoticeCreateCommand.from(request));
    }

    @LoginRequest
    @PostMapping("/{noticeId}/likes")
    public void addLike(@LoginUser User user,
                        @PathVariable Long noticeId)
    {
        noticeService.addLike(noticeId, user.getId());
    }

    @LoginRequest
    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId)
    {
        noticeService.delete(noticeId);
    }

    @LoginRequest
    @PutMapping
    public void updateNotice(@Valid @RequestBody NoticeUpdateRequest request)
    {
        noticeService.update(NoticeUpdateCommand.from(request));
    }
}
