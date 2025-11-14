package store.postHub.postHub.domain.community.service.result;

import lombok.Builder;
import lombok.Getter;
import store.postHub.postHub.domain.answer.service.result.AnswerWithCommentsResult;
import store.postHub.postHub.domain.community.mapper.record.CommunityRecord;

import java.util.List;

@Getter
@Builder
public class CommunityWithAnswersAndCommentsResult
{
    private CommunityRecord community;
    private boolean bookmarked;
    private List<AnswerWithCommentsResult> answersWithComments;


    public static CommunityWithAnswersAndCommentsResult from(CommunityRecord community, boolean bookmarked, List<AnswerWithCommentsResult> answersWithComments)
    {
        return CommunityWithAnswersAndCommentsResult.builder()
                .community(community)
                .bookmarked(bookmarked)
                .answersWithComments(answersWithComments)
                .build();
    }
}
