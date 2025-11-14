package store.postHub.postHub.domain.notice.service.result;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.domain.answer.service.result.AnswerWithCommentsResult;
import store.postHub.postHub.domain.notice.mapper.entity.Notice;

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
