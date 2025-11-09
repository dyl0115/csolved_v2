package store.csolved.csolved.domain.notice.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.Answer;
import store.csolved.csolved.domain.answer.AnswerWithComments;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.notice.Notice;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class NoticeDetailVM
{
    private Notice post;
    private List<AnswerWithComments> answers;

    public static NoticeDetailVM from(Notice notice,
                                      List<Answer> answers,
                                      Map<Long, List<Comment>> comments)
    {
        return NoticeDetailVM.builder()
                .answers(AnswerWithComments.from(answers, comments))
                .post(notice)
                .build();
    }
}
