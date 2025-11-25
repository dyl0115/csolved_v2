package store.babel.babel.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.answer.dto.AnswerWithComments;
import store.babel.babel.domain.answer.service.AnswerService;
import store.babel.babel.domain.category.dto.Category;
import store.babel.babel.domain.category.service.CategoryService;
import store.babel.babel.domain.notice.controller.dto.NoticeSearchRequest;
import store.babel.babel.domain.notice.dto.*;
import store.babel.babel.domain.notice.service.NoticeService;
import store.babel.babel.domain.post.dto.PostType;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.page.PageInfo;
import store.babel.babel.global.utils.page.Pagination;
import store.babel.babel.global.utils.search.SearchInfo;
import store.babel.babel.global.utils.search.Searching;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class NoticeController
{
    public final static String VIEWS_NOTICE_CREATE_FORM = "/views/notice/create";
    public final static String VIEWS_NOTICE_UPDATE_FORM = "/views/notice/update";
    public final static String VIEWS_NOTICE_LIST = "/views/notice/list";
    public final static String VIEWS_NOTICE_DETAIL = "/views/notice/detail";

    private final NoticeService noticeService;
    private final AnswerService answerService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/notices")
    public String getNoticeCards(NoticeSearchRequest request, Model model)
    {
        Long total = noticeService.countNotices(NoticeCountQuery.from(request));
        Pagination pagination = Pagination.from(request.getPage(), total);
        NoticeSearchQuery query = NoticeSearchQuery.from(request, pagination);
        List<NoticeCard> noticeCards = noticeService.getNoticeCards(query);

        model.addAttribute("notices", noticeCards);
        model.addAttribute("pagination", pagination);

        return VIEWS_NOTICE_LIST;
    }

    @LoginRequest
    @GetMapping("/notice/{noticeId}")
    public String getNotice(@PathVariable Long noticeId,
                            @RequestParam(required = false) Boolean skipView,
                            Model model)
    {
        if (skipView == null) noticeService.increaseView(noticeId);

        Notice notice = noticeService.getNotice(noticeId);
        List<AnswerWithComments> answersWithComments = answerService.getAnswersWithComments(noticeId);

        model.addAttribute("notice", notice);
        model.addAttribute("answersWithComments", answersWithComments);

        return VIEWS_NOTICE_DETAIL;
    }


    @LoginRequest
    @GetMapping("/notice/createForm")
    public String getNoticeCreateForm(Model model)
    {
        List<Category> categories = categoryService.getAllCategories(PostType.NOTICE.getValue());
        model.addAttribute("categories", categories);
        return VIEWS_NOTICE_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/notice/{noticeId}/updateForm")
    public String getNoticeUpdateForm(@PathVariable Long noticeId,
                                      Model model)
    {
        List<Category> categories = categoryService.getAllCategories(PostType.NOTICE.getValue());
        Notice notice = noticeService.getNotice(noticeId);

        model.addAttribute("categories", categories);
        model.addAttribute("notice", notice);
        return VIEWS_NOTICE_UPDATE_FORM;
    }
}
