package store.csolved.csolved.domain.notice.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.AnswerWithComments;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.domain.community.service.result.CommunityWithAnswersAndCommentsResult;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;

import java.util.List;

@Getter
@Builder
public class NoticeWithAnswersAndCommentsResult
{
    private Notice notice;
    private List<AnswerWithComments> answersWithComments;

    public static NoticeWithAnswersAndCommentsResult from(Notice notice, List<AnswerWithComments> answersWithComments)
    {
        return NoticeWithAnswersAndCommentsResult.builder()
                .notice(notice)
                .answersWithComments(answersWithComments)
                .build();
    }
}
