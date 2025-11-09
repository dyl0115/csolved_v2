package store.csolved.csolved.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.csolved.csolved.domain.answer.AnswerWithComments;
import store.csolved.csolved.domain.community.mapper.entity.Community;

import java.util.List;

@Getter
@Builder
public class CommunityWithAnswersAndCommentsResult
{
    private Community community;
    private boolean bookmarked;
    private List<AnswerWithComments> answersWithComments;


    public static CommunityWithAnswersAndCommentsResult from(Community community, boolean bookmarked, List<AnswerWithComments> answersWithComments)
    {
        return CommunityWithAnswersAndCommentsResult.builder()
                .community(community)
                .bookmarked(bookmarked)
                .answersWithComments(answersWithComments)
                .build();
    }
}
