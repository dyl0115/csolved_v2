package store.babel.babel.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.post.dto.PeriodType;
import store.babel.babel.domain.post.dto.PostSummary;

import java.util.List;

@Mapper
public interface PopularPostMapper
{
    List<PostSummary> getBestByPeriod(PeriodType periodType, Long offset, Long limit);

    List<PostSummary> getMostLiked(PeriodType periodType, Long limit);

    List<PostSummary> getMostViewed(PeriodType periodType, Long limit);

    List<PostSummary> getMostAnswered(PeriodType periodType, Long limit);
}
