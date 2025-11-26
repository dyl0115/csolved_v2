package store.babel.babel.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import store.babel.babel.domain.post.dto.PeriodType;
import store.babel.babel.domain.post.dto.PostSummary;

import java.util.List;

@Mapper
public interface PopularPostMapper
{
    Long countBestByPeriod(@Param("periodType") PeriodType periodType);

    List<PostSummary> getBestByPeriod(@Param("periodType") PeriodType periodType, Long offset, Long limit);

    List<PostSummary> getMostLiked(PeriodType periodType, Long limit);

    List<PostSummary> getMostViewed(PeriodType periodType, Long limit);

    List<PostSummary> getMostAnswered(PeriodType periodType, Long limit);
}
