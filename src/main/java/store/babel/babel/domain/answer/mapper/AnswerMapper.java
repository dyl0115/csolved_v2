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

    Answer getAnswerForAdmin(Long answerId);

    void increaseAnswerCount(Long postId);

    void decreaseAnswerCount(Long postId);

    boolean existComments(Long answerId);

    void deleteAnswer(Long answerId);

    void hardDelete(Long answerId);
}
