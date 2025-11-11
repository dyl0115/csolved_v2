package store.csolved.csolved.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;
import store.csolved.csolved.domain.answer.mapper.AnswerMapper;
import store.csolved.csolved.domain.answer.service.command.AnswerCreateCommand;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService
{
    private final AnswerMapper answerMapper;

    @Transactional
    public void saveAnswer(AnswerCreateCommand command)
    {
        answerMapper.increaseAnswerCount(command.getPostId());
        answerMapper.saveAnswer(Answer.from(command));
    }

    // 질문글에 대한 답변글들, 각각의 답변글에 대한 댓글들을 모두 반환.
    public List<Answer> getAnswers(Long questionId)
    {
        return answerMapper.getAnswers(questionId);
    }

    @Transactional
    public void delete(Long answerId)
    {
        boolean commentsExist = answerMapper.existComments(answerId);
        Answer answer = answerMapper.getAnswer(answerId);

        if (commentsExist)
        {
            answerMapper.softDelete(answerId);
        }
        else
        {
            answerMapper.decreaseAnswerCount(answer.getPostId());
            answerMapper.hardDelete(answerId);
        }
    }
}
