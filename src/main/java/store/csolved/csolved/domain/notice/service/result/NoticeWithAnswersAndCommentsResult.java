package store.csolved.csolved.domain.notice.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.service.result.AnswerWithCommentsResult;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;

import java.util.List;

@Getter
@Builder
public class NoticeWithAnswersAndCommentsResult
{
    private Notice notice;
    private List<AnswerWithCommentsResult> answersWithComments;

    public static NoticeWithAnswersAndCommentsResult from(Notice notice, List<AnswerWithCommentsResult> answersWithComments)
    {
        return NoticeWithAnswersAndCommentsResult.builder()
                .notice(notice)
                .answersWithComments(answersWithComments)
                .build();
    }
}
