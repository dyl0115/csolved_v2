package store.babel.babel.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.service.result.AnswerDetailResult;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.mapper.record.CommentResult;
import store.babel.babel.domain.notice.mapper.entity.Notice;
import store.babel.babel.domain.notice.mapper.NoticeMapper;
import store.babel.babel.domain.notice.service.command.NoticeCreateCommand;
import store.babel.babel.domain.notice.service.command.NoticeUpdateCommand;
import store.babel.babel.domain.notice.service.result.NoticeResult;
import store.babel.babel.domain.notice.service.result.NoticeWithAnswersAndCommentsResult;
import store.babel.babel.domain.notice.service.result.NoticesAndPageResult;
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
    //    private final PaginationManager paginationManager;
    private final NoticeMapper noticeMapper;
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;

    public Long countNotices(Searching search)
    {
        return noticeMapper.countNotices(NOTICE.getCode(), search);
    }

    @Transactional
    public void saveNotice(NoticeCreateCommand command)
    {
        noticeMapper.saveNotice(NOTICE.getCode(), Notice.from(command));
    }

    public List<Notice> getNotices(Pagination page, Searching search)
    {
        return noticeMapper.getNotices(NOTICE.getCode(), page, search);
    }

    public NoticeResult getNotice(Long noticeId)
    {
        Notice notice = noticeMapper.getNotice(noticeId);
        return NoticeResult.from(notice);
    }

    public NoticesAndPageResult getNoticesAndPage(Long pageNumber, Searching search)
    {

        Long totalPage = countNotices(search);
        Pagination page = Pagination.from(pageNumber, totalPage);
        List<Notice> notices = noticeMapper.getNotices(NOTICE.getCode(), page, search);

        return NoticesAndPageResult.from(notices, page);
    }

    @Transactional
    public Notice viewNotice(Long noticeId)
    {
        noticeMapper.increaseView(noticeId);
        return noticeMapper.getNotice(noticeId);
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateCommand command)
    {
        noticeMapper.updateNotice(noticeId, Notice.from(command));
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

    public NoticeWithAnswersAndCommentsResult getNoticeWithAnswersAndComments(Long noticeId)
    {
        Notice notice = noticeMapper.getNotice(noticeId);
        List<AnswerDetailResult> answersWithComments = getAnswersWithComments(noticeId);
        return NoticeWithAnswersAndCommentsResult.from(notice, answersWithComments);
    }

    private List<AnswerDetailResult> getAnswersWithComments(Long noticeId)
    {
        List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answers = answerMapper.getAnswers(noticeId);
        Map<Long, List<CommentResult>> answerWithCommentsMap = mapCommentsToAnswer(extractIds(answers));
        return AnswerDetailResult.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<CommentResult>> mapCommentsToAnswer(List<Long> answerIds)
    {
        List<CommentResult> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(CommentResult::getAnswerId));
    }

    private List<Long> extractIds(List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answers)
    {
        return answers.stream()
                .map(store.babel.babel.domain.answer.mapper.record.AnswerDetailResult::getId)
                .toList();
    }
}
