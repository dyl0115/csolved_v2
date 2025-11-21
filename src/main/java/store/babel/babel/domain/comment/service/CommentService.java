package store.babel.babel.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.comment.dto.CommentCreateCommand;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.post.mapper.PostMapper;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService
{
    private final CommentMapper commentMapper;
    private final PostMapper postMapper;

    @Transactional
    public void saveComment(CommentCreateCommand command)
    {
        postMapper.increaseAnswerCount(command.getPostId());
        commentMapper.saveComment(command);
    }

    @Transactional
    public void delete(Long userId, Long commentId)
    {
        if (!Objects.equals(commentMapper.getAuthorId(commentId), userId))
        {
            throw new BabelException(ExceptionCode.ACCESS_DENIED);
        }
        postMapper.decreaseAnswerCount(commentMapper.getPostId(commentId));
        commentMapper.deleteComment(commentId);
    }
}
