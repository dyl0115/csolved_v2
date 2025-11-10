package store.csolved.csolved.domain.answer.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;

import java.util.List;

@Mapper
public interface AnswerMapper
{
    Long saveAnswer(Answer answer);

    List<Answer> getAnswers(Long postId);

    Answer getAnswer(Long answerId);

    void increaseAnswerCount(Long postId);

    void decreaseAnswerCount(Long postId);

//    Long getScore(Long answerId, Long userId);

//    void saveScore(Long answerId, Long score);
//
//    void saveVoter(Long answerId, Long userId, Long score);
//
//    void updateVoter(Long answerId, Long userId, Long score);

    boolean existComments(Long answerId);

    void hardDeleteScores(Long answerId);

    void softDelete(Long answerId);

    void hardDelete(Long answerId);
}
