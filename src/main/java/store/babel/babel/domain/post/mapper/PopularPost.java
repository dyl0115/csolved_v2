package store.babel.babel.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.post.dto.PeriodType;
import store.babel.babel.domain.post.dto.PostCard;

import java.util.List;

@Mapper
public interface PopularPost
{
    List<PostCard> getBestByPeriod(PeriodType periodType, Long limit);

    List<PostCard> getMostLiked(PeriodType periodType, Long limit);

    List<PostCard> getMostViewed(PeriodType periodType, Long limit);

    List<PostCard> getMostAnswered(PeriodType periodType, Long limit);
}
