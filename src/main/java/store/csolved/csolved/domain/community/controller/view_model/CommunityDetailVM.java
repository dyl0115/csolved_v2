package store.csolved.csolved.domain.community.controller.view_model;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.Answer;
import store.csolved.csolved.domain.answer.AnswerWithComments;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.community.mapper.entity.Community;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class CommunityDetailVM
{
    private Community post;
    private boolean bookmarked;
    private List<AnswerWithComments> answers;

    public static CommunityDetailVM from(Community community,
                                         boolean bookmarked,
                                         List<Answer> answers,
                                         Map<Long, List<Comment>> comments)
    {
        return CommunityDetailVM.builder()
                .bookmarked(bookmarked)
                .answers(AnswerWithComments.from(answers, comments))
                .post(community)
                .build();
    }
}
