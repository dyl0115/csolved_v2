package store.babel.babel.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.post.dto.PeriodType;
import store.babel.babel.domain.post.dto.PostSummary;
import store.babel.babel.domain.post.mapper.PopularPostMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularPostService
{
    private final PopularPostMapper popularPostMapper;

    @Transactional(readOnly = true)
    public List<PostSummary> getBestByPeriod(PeriodType periodType, Long offset, Long limit)
    {
        return popularPostMapper.getBestByPeriod(periodType, offset, limit);
    }

    @Transactional(readOnly = true)
    public List<PostSummary> getMostLiked(PeriodType periodType, Long limit)
    {
        return popularPostMapper.getMostLiked(periodType, limit);
    }

    @Transactional(readOnly = true)
    public List<PostSummary> getMostViewed(PeriodType periodType, Long limit)
    {
        return popularPostMapper.getMostViewed(periodType, limit);
    }

    @Transactional(readOnly = true)
    public List<PostSummary> getMostAnswered(PeriodType periodType, Long limit)
    {
        return popularPostMapper.getMostAnswered(periodType, limit);
    }
}
