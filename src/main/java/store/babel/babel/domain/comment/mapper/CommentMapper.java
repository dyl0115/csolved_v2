package store.babel.babel.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.comment.dto.CommentCreateCommand;
import store.babel.babel.domain.comment.dto.Comment;

import java.util.List;

@Mapper
public interface CommentMapper
{
    void save(CommentCreateCommand comment);

    List<Comment> getComments(List<Long> answerIds);

    Long getAuthorId(Long commentId);

    void delete(Long commentId);
}
