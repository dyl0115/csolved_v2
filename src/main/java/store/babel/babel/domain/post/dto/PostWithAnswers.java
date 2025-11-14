package store.babel.babel.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.answer.service.result.AnswerDetailResult;

import java.util.List;

@Getter
@Builder
public class PostWithAnswers
{
    private Post post;
    private boolean bookmarked;
    private List<AnswerDetailResult> answersWithComments;


    public static PostWithAnswers from(Post post, boolean bookmarked, List<AnswerDetailResult> answersWithComments)
    {
        return PostWithAnswers.builder()
                .post(post)
                .bookmarked(bookmarked)
                .answersWithComments(answersWithComments)
                .build();
    }
}
