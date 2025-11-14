package store.babel.babel.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.comment.mapper.param.CommentCreateParam;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.mapper.record.CommentResult;
import store.babel.babel.domain.comment.service.command.CommentCreateCommand;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService
{
    private final CommentMapper commentMapper;

    @Transactional
    public void saveComment(CommentCreateCommand command)
    {
        commentMapper.save(CommentCreateParam.from(command));
    }

    public Map<Long, List<CommentResult>> getComments(List<Long> answerIds)
    {
        List<CommentResult> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(CommentResult::getAnswerId));
    }

    @Transactional
    public void delete(Long userId, Long commentId)
    {
        CommentResult comment = commentMapper.getComment(commentId);
        if (!Objects.equals(comment.getAuthorId(), userId))
        {
            throw new BabelException(ExceptionCode.ACCESS_DENIED);
        }
        commentMapper.delete(commentId);
    }
}
