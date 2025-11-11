package store.csolved.csolved.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.AnswerWithComments;
import store.csolved.csolved.domain.answer.mapper.AnswerMapper;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.comment.mapper.CommentMapper;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.domain.community.service.result.CommunityWithAnswersAndCommentsResult;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;
import store.csolved.csolved.domain.notice.mapper.NoticeMapper;
import store.csolved.csolved.domain.notice.service.command.NoticeCreateCommand;
import store.csolved.csolved.domain.notice.service.command.NoticeUpdateCommand;
import store.csolved.csolved.domain.notice.service.result.NoticeResult;
import store.csolved.csolved.domain.notice.service.result.NoticeWithAnswersAndCommentsResult;
import store.csolved.csolved.domain.notice.service.result.NoticesAndPageResult;
import store.csolved.csolved.utils.page.Pagination;
import store.csolved.csolved.utils.page.PaginationManager;
import store.csolved.csolved.utils.search.Searching;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static store.csolved.csolved.common.PostType.NOTICE;

@RequiredArgsConstructor
@Service
public class NoticeService
{
    private final PaginationManager paginationManager;
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
        Pagination page = paginationManager.createPagination(pageNumber, totalPage);
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
    public boolean addLike(Long noticeId, Long userId)
    {
        if (noticeMapper.hasUserLiked(noticeId, userId))
        {
            return false;
        }

        noticeMapper.addUserLike(noticeId, userId);
        noticeMapper.increaseLikes(noticeId);
        return true;
    }

    public NoticeWithAnswersAndCommentsResult getNoticeWithAnswersAndComments(Long noticeId)
    {
        Notice notice = noticeMapper.getNotice(noticeId);
        List<AnswerWithComments> answersWithComments = getAnswersWithComments(noticeId);
        return NoticeWithAnswersAndCommentsResult.from(notice, answersWithComments);
    }

    private List<AnswerWithComments> getAnswersWithComments(Long noticeId)
    {
        List<Answer> answers = answerMapper.getAnswers(noticeId);
        Map<Long, List<Comment>> answerWithCommentsMap = mapCommentsToAnswer(extractIds(answers));
        return AnswerWithComments.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<Comment>> mapCommentsToAnswer(List<Long> answerIds)
    {
        List<Comment> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(Comment::getAnswerId));
    }

    private List<Long> extractIds(List<Answer> answers)
    {
        return answers.stream()
                .map(Answer::getId)
                .toList();
    }
}
