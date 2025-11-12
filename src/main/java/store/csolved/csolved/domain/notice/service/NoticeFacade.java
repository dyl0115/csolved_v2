package store.csolved.csolved.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;
import store.csolved.csolved.domain.answer.service.AnswerService;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.comment.service.CommentService;
import store.csolved.csolved.domain.notice.mapper.entity.Notice;
import store.csolved.csolved.domain.notice.controller.request.NoticeCreateRequest;
import store.csolved.csolved.domain.notice.controller.view_model.NoticeDetailVM;
import store.csolved.csolved.domain.notice.controller.view_model.NoticeListVM;
import store.csolved.csolved.global.utils.page.Pagination;
import store.csolved.csolved.global.utils.page.PaginationManager;
import store.csolved.csolved.global.utils.search.Searching;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NoticeFacade
{
    private final NoticeService noticeService;
    private final AnswerService answerService;
    private final PaginationManager paginationUtils;
    private final CommentService commentService;

    public NoticeListVM getNotices(Long pageNumber, Searching search)
    {
        Long total = noticeService.countNotices(search);
        Pagination page = paginationUtils.createPagination(pageNumber, total);
        List<Notice> notices = noticeService.getNotices(page, search);
        return NoticeListVM.from(page, notices);
    }

    public NoticeDetailVM viewNotice(Long postId)
    {
        Notice notice = noticeService.viewNotice(postId);
        List<Answer> answers = answerService.getAnswers(postId);
        Map<Long, List<Comment>> comments = commentService.getComments(extractIds(answers));
        return NoticeDetailVM.from(notice, answers, comments);
    }

    public NoticeDetailVM getNotice(Long postId)
    {
//        Notice notice = noticeService.getNotice(postId);
//        List<Answer> answers = answerService.getAnswers(postId);
//        Map<Long, List<Comment>> comments = commentService.getComments(extractIds(answers));
//        return NoticeDetailVM.from(notice, answers, comments);
        return null;
    }

    private List<Long> extractIds(List<Answer> answers)
    {
        return answers.stream()
                .map(Answer::getId)
                .toList();
    }

    @Transactional
    public void update(Long postId, NoticeCreateRequest form)
    {
//        noticeService.update(postId, form.getNotice());
    }

    public NoticeCreateRequest initUpdateForm(Long postId)
    {
//        Notice notice = noticeService.getNotice(postId);
//        return NoticeCreateRequest.from(notice);
        return null;
    }

    @Transactional
    public boolean addLike(Long postId, Long userId)
    {
        return noticeService.addLike(postId, userId);
    }

    @Transactional
    public void delete(Long postId)
    {
        noticeService.delete(postId);
    }
}
