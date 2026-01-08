package store.csolved.csolved.domain.notice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.notice.controller.dto.NoticeCreateRequest;
import store.csolved.csolved.domain.notice.controller.dto.NoticeUpdateRequest;
import store.csolved.csolved.domain.notice.service.NoticeService;
import store.csolved.csolved.domain.notice.dto.NoticeCreateCommand;
import store.csolved.csolved.domain.notice.dto.NoticeUpdateCommand;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;

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
