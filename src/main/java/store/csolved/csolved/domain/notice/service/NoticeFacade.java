package store.csolved.csolved.domain.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;
import store.csolved.csolved.domain.answer.service.AnswerService;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.comment.service.CommentService;
import store.csolved.csolved.domain.notice.Notice;
import store.csolved.csolved.domain.notice.controller.form.NoticeCreateUpdateForm;
import store.csolved.csolved.domain.notice.controller.view_model.NoticeDetailVM;
import store.csolved.csolved.domain.notice.controller.view_model.NoticeListVM;
import store.csolved.csolved.utils.page.Pagination;
import store.csolved.csolved.utils.page.PaginationManager;
import store.csolved.csolved.utils.search.Searching;

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

    @Transactional
    public void save(NoticeCreateUpdateForm form)
    {
        noticeService.save(form.getNotice());
    }

    public NoticeListVM getNotices(Long pageNumber, Searching search)
    {
        Long total = noticeService.countNotice(search);
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
        Notice notice = noticeService.getNotice(postId);
        List<Answer> answers = answerService.getAnswers(postId);
        Map<Long, List<Comment>> comments = commentService.getComments(extractIds(answers));
        return NoticeDetailVM.from(notice, answers, comments);
    }

    private List<Long> extractIds(List<Answer> answers)
    {
        return answers.stream()
                .map(Answer::getId)
                .toList();
    }

    @Transactional
    public void update(Long postId, NoticeCreateUpdateForm form)
    {
        noticeService.update(postId, form.getNotice());
    }

    public NoticeCreateUpdateForm initUpdateForm(Long postId)
    {
        Notice notice = noticeService.getNotice(postId);
        return NoticeCreateUpdateForm.from(notice);
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
