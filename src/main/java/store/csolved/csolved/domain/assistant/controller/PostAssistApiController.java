package store.csolved.csolved.domain.assistant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.csolved.csolved.domain.assistant.dto.PostAssistRequest;
import store.csolved.csolved.domain.assistant.service.PostAssistService;
import store.csolved.csolved.domain.assistant.session.AssistantChatSession;
import store.csolved.csolved.domain.assistant.session.ChatSession;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;

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
