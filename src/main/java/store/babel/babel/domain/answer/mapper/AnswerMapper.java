package store.babel.babel.domain.answer.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.answer.dto.AnswerCreateCommand;
import store.babel.babel.domain.answer.dto.Answer;

import java.util.List;

@Mapper
public interface AnswerMapper
{
    void saveAnswer(AnswerCreateCommand command);

    List<Answer> getAnswers(Long postId);

    Answer getAnswer(Long answerId);

    Long getPostId(Long answerId);

    Long getAuthorId(Long answerId);

    Answer getAnswerForAdmin(Long answerId);

    boolean existComments(Long answerId);

    void deleteAnswer(Long answerId);

    void deleteAnswerByAdmin(Long answerId);

    void restoreAnswerByAdmin(Long answerId);

    boolean hasUserLike(Long answerId, Long userId);

    void increaseLikes(Long answerId);

    void addUserLike(Long answerId, Long userId);
}
