package store.csolved.csolved.domain.comment.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.comment.dto.CommentCreateCommand;
import store.csolved.csolved.domain.comment.dto.Comment;

import java.util.List;

@Mapper
public interface CommentMapper
{
    void saveComment(CommentCreateCommand comment);

    List<Comment> getComments(List<Long> answerIds);

    Long getAuthorId(Long commentId);

    Long getPostId(Long commentId);

    void deleteComment(Long commentId);

    void deleteCommentByAdmin(Long commentId);

    void restoreCommentByAdmin(Long commentId);

    Comment getCommentForAdmin(Long commentId);
}
