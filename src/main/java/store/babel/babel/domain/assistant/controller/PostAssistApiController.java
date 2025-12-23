package store.babel.babel.domain.assistant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.assistant.dto.PostAssistRequest;
import store.babel.babel.domain.assistant.service.PostAssistService;
import store.babel.babel.domain.assistant.session.AssistantChatSession;
import store.babel.babel.domain.assistant.session.ChatSession;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

@RequiredArgsConstructor
@RequestMapping("/api/ai/post")
@Controller
public class PostAssistApiController
{
    private final PostAssistService postAssistService;

    @LoginRequest
    @GetMapping("/connect")
    public SseEmitter connect(@LoginUser User user)
    {
        return postAssistService.connect(user.getId());
    }

    @LoginRequest
    @ResponseBody
    @PostMapping("/message")
    public void assistPost(@ChatSession AssistantChatSession session,
                           @RequestBody PostAssistRequest request)
    {
        postAssistService.assistPost(session, request);
    }
}
