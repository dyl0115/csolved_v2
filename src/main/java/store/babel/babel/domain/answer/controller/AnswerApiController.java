package store.babel.babel.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.answer.dto.AnswerCreateCommand;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.domain.answer.controller.dto.AnswerCreateRequest;
import store.babel.babel.domain.answer.service.AnswerService;

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
    public void deleteAnswer(@PathVariable Long answerId)
    {
        answerService.deleteAnswer(answerId);
    }
}