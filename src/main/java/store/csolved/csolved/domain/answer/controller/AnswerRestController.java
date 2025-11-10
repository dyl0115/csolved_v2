package store.csolved.csolved.domain.answer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.domain.answer.service.AnswerService;

@RequestMapping("/api/answers")
@RequiredArgsConstructor
@RestController
public class AnswerRestController
{
    private final AnswerService answerService;

//    @LoginRequest
//    @PostMapping("/{answerId}/score")
//    @ResponseStatus(HttpStatus.OK)
//    public AnswerScoreResponse saveScore(@LoginUser User user,
//                                         @PathVariable Long answerId,
//                                         @RequestBody Long score)
//    {
//        Long prevScore = answerService.getScore(answerId, user.getId());
//        if (prevScore != null)
//        {
//            return AnswerScoreResponse.duplicate(prevScore);
//        }
//
//        Answer answer = answerService.saveScore(answerId, user.getId(), score);
//        return AnswerScoreResponse.success(answer);
//    }

//    @LoginRequest
//    @PutMapping("/{answerId}/score")
//    @ResponseStatus(HttpStatus.OK)
//    public AnswerScoreResponse updateScore(@LoginUser User user,
//                                           @PathVariable Long answerId,
//                                           @RequestBody Long score)
//    {
//        Answer answer = answerService.updateScore(answerId, user.getId(), score);
//        return AnswerScoreResponse.success(answer);
//    }

    @LoginRequest
    @DeleteMapping("/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAnswer(@PathVariable Long answerId)
    {
        answerService.delete(answerId);
    }
}
