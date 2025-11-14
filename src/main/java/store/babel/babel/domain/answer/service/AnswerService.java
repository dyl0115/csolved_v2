package store.babel.babel.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.mapper.param.AnswerCreateParam;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.answer.service.command.AnswerCreateCommand;

@RequiredArgsConstructor
@Service
public class AnswerService
{
    private final AnswerMapper answerMapper;

    @Transactional
    public void saveAnswer(AnswerCreateCommand command)
    {
        answerMapper.increaseAnswerCount(command.getPostId());
        answerMapper.saveAnswer(AnswerCreateParam.from(command));
    }

    @Transactional
    public void deleteAnswer(Long answerId)
    {
        boolean commentsExist = answerMapper.existComments(answerId);
        AnswerCreateParam answer = answerMapper.getAnswer(answerId);

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
