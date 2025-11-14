package store.babel.babel.domain.notice.service.result;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.answer.service.result.AnswerDetailResult;
import store.babel.babel.domain.notice.mapper.entity.Notice;

import java.util.List;

@Getter
@Builder
public class NoticeWithAnswersAndCommentsResult
{
    private Notice notice;
    private List<AnswerDetailResult> answersWithComments;

    public static NoticeWithAnswersAndCommentsResult from(Notice notice, List<AnswerDetailResult> answersWithComments)
    {
        return NoticeWithAnswersAndCommentsResult.builder()
                .notice(notice)
                .answersWithComments(answersWithComments)
                .build();
    }
}
