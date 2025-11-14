package store.postHub.postHub.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.answer.service.command.AnswerCreateCommand;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.domain.answer.controller.request.AnswerCreateRequest;
import store.postHub.postHub.domain.answer.service.AnswerService;

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