package store.postHub.postHub.domain.notice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.notice.controller.request.NoticeCreateRequest;
import store.postHub.postHub.domain.notice.controller.request.NoticeUpdateRequest;
import store.postHub.postHub.domain.notice.service.NoticeService;
import store.postHub.postHub.domain.notice.service.command.NoticeCreateCommand;
import store.postHub.postHub.domain.notice.service.command.NoticeUpdateCommand;
import store.postHub.postHub.domain.user.User;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.global.utils.login.LoginUser;

@RequestMapping("/api/notice")
@RequiredArgsConstructor
@RestController
public class NoticeRestController
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
    @PutMapping("/{noticeId}")
    public void updateNotice(@PathVariable("noticeId") Long noticeId,
                             @Valid @RequestBody NoticeUpdateRequest request)
    {
        noticeService.update(noticeId, NoticeUpdateCommand.from(request));
    }
}
