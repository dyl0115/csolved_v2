package store.postHub.postHub.domain.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.notice.service.NoticeService;
import store.postHub.postHub.domain.notice.service.result.NoticeResult;
import store.postHub.postHub.domain.notice.service.result.NoticeWithAnswersAndCommentsResult;
import store.postHub.postHub.domain.notice.service.result.NoticesAndPageResult;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.global.utils.page.PageInfo;
import store.postHub.postHub.global.utils.search.SearchInfo;
import store.postHub.postHub.global.utils.search.Searching;

@RequiredArgsConstructor
@Controller
public class NoticeController
{
    public final static String VIEWS_NOTICE_CREATE_FORM = "/views/notice/create";
    public final static String VIEWS_NOTICE_UPDATE_FORM = "/views/notice/update";
    public final static String VIEWS_NOTICE_LIST = "/views/notice/list";
    public final static String VIEWS_NOTICE_DETAIL = "/views/notice/detail";

    private final NoticeService noticeService;

    @LoginRequest
    @GetMapping("/notices")
    public String getNotices(@PageInfo Long pageNumber,
                             @SearchInfo Searching search,
                             Model model)
    {
        NoticesAndPageResult noticesAndPage = noticeService.getNoticesAndPage(pageNumber, search);
        model.addAttribute("notices", noticesAndPage.getNotices());
        model.addAttribute("page", noticesAndPage.getPage());

        return VIEWS_NOTICE_LIST;
    }

    @LoginRequest
    @GetMapping("/notice/{noticeId}")
    public String getNotice(@PathVariable Long noticeId,
                            Model model)
    {
        NoticeWithAnswersAndCommentsResult result = noticeService.getNoticeWithAnswersAndComments(noticeId);

        model.addAttribute("notice", result.getNotice());
        model.addAttribute("answersWithComments", result.getAnswersWithComments());

        return VIEWS_NOTICE_DETAIL;
    }


    @LoginRequest
    @GetMapping("/notice/createForm")
    public String getNoticeCreateForm()
    {
        return VIEWS_NOTICE_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/notice/{noticeId}/updateForm")
    public String getNoticeUpdateForm(@PathVariable Long noticeId,
                                      Model model)
    {
        NoticeResult result = noticeService.getNotice(noticeId);
        model.addAttribute("notice", result);
        return VIEWS_NOTICE_UPDATE_FORM;
    }


}
