package store.csolved.csolved.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.comment.mapper.param.CommentCreateParam;
import store.csolved.csolved.domain.comment.mapper.record.CommentDetailRecord;

import java.util.List;

@Mapper
public interface CommentMapper
{
    void save(CommentCreateParam comment);

    List<CommentDetailRecord> getComments(List<Long> answerIds);

    CommentDetailRecord getComment(Long commentId);

    void delete(Long commentId);
}
