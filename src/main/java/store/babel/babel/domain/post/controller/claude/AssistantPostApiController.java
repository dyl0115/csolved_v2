package store.babel.babel.domain.post.controller.claude;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.post.service.ClaudePostService;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

@RequiredArgsConstructor
@RequestMapping("/api/ai/post")
@Controller
public class AssistantPostApiController
{
    private final ClaudePostService claudePostService;

    @LoginRequest
    @GetMapping("/connect")
    public SseEmitter connect(@LoginUser User user)
    {
        return claudePostService.connect(user.getId());
    }

    @LoginRequest
    @ResponseBody
    @PostMapping("/message")
    public void assistPost(@LoginUser User user,
                           @RequestBody ClaudeMessage message)
    {
        claudePostService.assistPost(user.getId(), message);
    }
}
