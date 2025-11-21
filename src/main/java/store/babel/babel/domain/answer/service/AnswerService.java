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
import store.babel.babel.domain.post.mapper.PostMapper;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnswerService
{
    private static final String DELETED_CONTENT_MESSAGE = "[삭제된 댓글 입니다.]";

    private final AnswerMapper answerMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Transactional
    public void saveAnswer(AnswerCreateCommand command)
    {
        postMapper.increaseAnswerCount(command.getPostId());
        answerMapper.saveAnswer(command);
    }

    @Transactional
    public void deleteAnswer(Long answerId, Long userId)
    {
        Answer answer = answerMapper.getAnswer(answerId);

        if (answer == null)
        {
            throw new BabelException(ExceptionCode.ANSWER_NOT_FOUND);
        }

        if (!Objects.equals(answer.getAuthorId(), userId))
        {
            throw new BabelException(ExceptionCode.ANSWER_DELETE_DENIED);
        }

        postMapper.decreaseAnswerCount(answer.getPostId());
        answerMapper.deleteAnswer(answerId);
    }

    public List<AnswerWithComments> getAnswersWithComments(Long postId)
    {
        List<Answer> answers = answerMapper.getAnswers(postId);
        Map<Long, List<Comment>> commentsByAnswer = mapAnswerIdToComments(extractIds(answers));
        removeDeletedAnswersWithoutComments(answers, commentsByAnswer);
        maskDeletedAnswersWithComments(answers, commentsByAnswer);
        return AnswerWithComments.from(answers, commentsByAnswer);
    }

    private void removeDeletedAnswersWithoutComments(List<Answer> answers, Map<Long, List<Comment>> commentsByAnswer)
    {
        answers.removeIf(answer -> isDeletedAndHasNoComments(answer, commentsByAnswer));
    }

    private void maskDeletedAnswersWithComments(List<Answer> answers, Map<Long, List<Comment>> commentByAnswer)
    {
        answers.stream()
                .filter(answer -> isDeletedAndHasComments(answer, commentByAnswer))
                .forEach(answer -> answer.maskContent(DELETED_CONTENT_MESSAGE));
    }

    private boolean isDeletedAndHasComments(Answer answer, Map<Long, List<Comment>> commentByAnswer)
    {
        return answer.isDeleted() && hasComments(answer.getId(), commentByAnswer);
    }

    private boolean isDeletedAndHasNoComments(Answer answer, Map<Long, List<Comment>> commentsByAnswer)
    {
        return answer.isDeleted() && !hasComments(answer.getId(), commentsByAnswer);
    }

    private boolean hasComments(Long answerId, Map<Long, List<Comment>> commentsByAnswer)
    {
        return !commentsByAnswer.getOrDefault(answerId, Collections.emptyList()).isEmpty();
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
