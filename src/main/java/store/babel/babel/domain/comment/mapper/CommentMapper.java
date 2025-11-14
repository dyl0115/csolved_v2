package store.babel.babel.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.comment.mapper.param.CommentCreateParam;
import store.babel.babel.domain.comment.mapper.record.CommentResult;

import java.util.List;

@Mapper
public interface CommentMapper
{
    void save(CommentCreateParam comment);

    List<CommentResult> getComments(List<Long> answerIds);

    CommentResult getComment(Long commentId);

    void delete(Long commentId);
}
