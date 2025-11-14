package store.babel.babel.domain.answer.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.answer.mapper.param.AnswerCreateParam;
import store.babel.babel.domain.answer.mapper.record.AnswerDetailRecord;

import java.util.List;

@Mapper
public interface AnswerMapper
{
    Long saveAnswer(AnswerCreateParam answer);

    List<AnswerDetailRecord> getAnswers(Long postId);

    AnswerCreateParam getAnswer(Long answerId);

    void increaseAnswerCount(Long postId);

    void decreaseAnswerCount(Long postId);

    boolean existComments(Long answerId);

    void softDelete(Long answerId);

    void hardDelete(Long answerId);
}
