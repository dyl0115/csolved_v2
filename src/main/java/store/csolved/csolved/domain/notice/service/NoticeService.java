package store.csolved.csolved.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.notice.dto.*;
import store.csolved.csolved.domain.notice.mapper.NoticeMapper;
import store.csolved.csolved.global.exception.BabelException;
import store.csolved.csolved.global.exception.ExceptionCode;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeService
{
    private final NoticeMapper noticeMapper;

    public Long countNotices(NoticeCountQuery query)
    {
        return noticeMapper.countNotices(query);
    }

    @Transactional
    public void saveNotice(NoticeCreateCommand command)
    {
        noticeMapper.saveNotice(command);
    }

    public Notice getNotice(Long noticeId)
    {
        return noticeMapper.getNotice(noticeId);
    }

    public List<NoticeCard> getNoticeCards(NoticeSearchQuery query)
    {
        return noticeMapper.getNoticeCards(query);
    }

    @Transactional
    public void update(NoticeUpdateCommand command)
    {
        noticeMapper.updateNotice(command);
    }

    @Transactional
    public void delete(Long noticeId)
    {
        noticeMapper.deleteNotice(noticeId);
    }

    @Transactional
    public void addLike(Long noticeId, Long userId)
    {
        if (noticeMapper.hasUserLiked(noticeId, userId))
        {
            throw new BabelException(ExceptionCode.ALREADY_LIKED);
        }

        noticeMapper.addUserLike(noticeId, userId);
        noticeMapper.increaseLikes(noticeId);
    }

    @Transactional
    public void increaseView(Long noticeId)
    {
        noticeMapper.increaseView(noticeId);
    }
}
