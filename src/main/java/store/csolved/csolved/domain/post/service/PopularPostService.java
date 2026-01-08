package store.csolved.csolved.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.post.dto.PeriodType;
import store.csolved.csolved.domain.post.dto.PostSummary;
import store.csolved.csolved.domain.post.mapper.PopularPostMapper;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularPostService
{
    private final PopularPostMapper popularPostMapper;

    @Transactional(readOnly = true)
    public Long countBestByPeriod(PeriodType periodType)
    {
        return popularPostMapper.countBestByPeriod(periodType);
    }

    @Transactional(readOnly = true)
    public List<PostSummary> getBestByPeriod(PeriodType periodType, Long offset, Long limit)
    {
        return popularPostMapper.getBestByPeriod(periodType, offset, limit);
    }

    @Transactional(readOnly = true)
    public List<PostSummary> getBestByPeriod(PeriodType periodType, Pagination pagination)
    {
        return popularPostMapper.getBestByPeriod(periodType, pagination.getOffset(), pagination.getSize());
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
