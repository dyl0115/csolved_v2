package store.babel.babel.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.comment.mapper.param.CommentCreateParam;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.mapper.record.CommentDetailRecord;
import store.babel.babel.domain.comment.service.command.CommentCreateCommand;
import store.babel.babel.domain.comment.service.result.CommentDetailResult;
import store.babel.babel.global.exception.CsolvedException;
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

    public Map<Long, List<CommentDetailResult>> getComments(List<Long> answerIds)
    {
        List<CommentDetailRecord> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .map(CommentDetailResult::from)
                .collect(Collectors.groupingBy(CommentDetailResult::getAnswerId));
    }

    @Transactional
    public void delete(Long userId, Long commentId)
    {
        CommentDetailRecord comment = commentMapper.getComment(commentId);
        if (!Objects.equals(comment.getAuthorId(), userId))
        {
            throw new CsolvedException(ExceptionCode.ACCESS_DENIED);
        }
        commentMapper.delete(commentId);
    }
}
