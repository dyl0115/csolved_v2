package store.babel.babel.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.dto.Answer;
import store.babel.babel.domain.answer.dto.AnswerCreateCommand;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.answer.dto.AnswerWithComments;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.dto.Comment;

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
        answerMapper.saveAnswer(command);
    }

    @Transactional
    public void deleteAnswer(Long answerId)
    {
        boolean commentsExist = answerMapper.existComments(answerId);
        AnswerCreateCommand answer = answerMapper.getAnswer(answerId);

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

    public List<AnswerWithComments> getAnswersWithComments(Long postId)
    {
        List<Answer> answers = answerMapper.getAnswers(postId);
        Map<Long, List<Comment>> answerWithCommentsMap = mapAnswerIdToComments(extractIds(answers));
        return AnswerWithComments.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<Comment>> mapAnswerIdToComments(List<Long> answerIds)
    {
        List<Comment> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(Comment::getAnswerId));
    }

    private List<Long> extractIds(List<Answer> answers)
    {
        return answers.stream()
                .map(Answer::getId)
                .toList();
    }
}
