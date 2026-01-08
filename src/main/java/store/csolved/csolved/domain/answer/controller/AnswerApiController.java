package store.csolved.csolved.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.answer.dto.AnswerCreateCommand;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.domain.answer.controller.dto.AnswerCreateRequest;
import store.csolved.csolved.domain.answer.service.AnswerService;
import store.csolved.csolved.global.utils.login.LoginUser;

@RequiredArgsConstructor
@RequestMapping("/api/answer")
@RestController
public class AnswerApiController
{
    private final AnswerService answerService;

    @LoginRequest
    @PostMapping
    public void saveAnswer(@Valid @RequestBody AnswerCreateRequest request)
    {
        answerService.saveAnswer(AnswerCreateCommand.from(request));
    }

    @LoginRequest
    @DeleteMapping("/{answerId}")
    public void deleteAnswer(@LoginUser User user,
                             @PathVariable Long answerId)
    {
        answerService.deleteAnswer(answerId, user.getId());
    }

    @LoginRequest
    @PostMapping("/{answerId}/likes")
    public void addAnswerLike(@LoginUser User user,
                              @PathVariable Long answerId)
    {
        answerService.addLike(answerId, user.getId());
    }
}