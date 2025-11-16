package store.babel.babel.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.dto.Answer;
import store.babel.babel.domain.answer.dto.AnswerWithComments;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.dto.Comment;
import store.babel.babel.domain.notice.dto.*;
import store.babel.babel.domain.notice.mapper.NoticeMapper;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.utils.page.Pagination;
import store.babel.babel.global.utils.search.Searching;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static store.babel.babel.common.PostType.NOTICE;

@RequiredArgsConstructor
@Service
public class NoticeService
{
    private final NoticeMapper noticeMapper;
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;

    public Long countNotices(NoticeSearchQuery query)
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

    public List<NoticeCard> getNoticeCards(NoticeSearchQuery query,
                                           Pagination pagination)
    {
        return noticeMapper.getNoticeCards(query, pagination);
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
}
