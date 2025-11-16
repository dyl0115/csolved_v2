package store.babel.babel.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.mapper.param.AnswerCreateParam;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.answer.service.command.AnswerCreateCommand;
import store.babel.babel.domain.answer.service.result.AnswerDetailResult;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.mapper.record.CommentResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnswerService
{
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;

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

    public List<AnswerDetailResult> getAnswersWithComments(Long postId)
    {
        List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answers = answerMapper.getAnswers(postId);
        Map<Long, List<CommentResult>> answerWithCommentsMap = mapAnswerIdToComments(extractIds(answers));
        return AnswerDetailResult.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<CommentResult>> mapAnswerIdToComments(List<Long> answerIds)
    {
        List<CommentResult> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(CommentResult::getAnswerId));
    }

    private List<Long> extractIds(List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answers)
    {
        return answers.stream()
                .map(store.babel.babel.domain.answer.mapper.record.AnswerDetailResult::getId)
                .toList();
    }
}
