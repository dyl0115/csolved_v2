package store.csolved.csolved.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.domain.user.service.UserActivityFacade;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;
import store.csolved.csolved.global.utils.page.PageInfo;

@RequestMapping("/bookmark")
@RequiredArgsConstructor
@Controller
public class BookmarkController
{
    private static final String FRAGMENT_BOOKMARK_LIST = "/views/user-profile/activity :: bookmarkList";

    private final UserActivityFacade userActivityFacade;

    @LoginRequest
    @GetMapping
    public String getBookmarksAndPage(@LoginUser User user,
                                      @PageInfo(type = "bookmarkPage") Long pageNumber,
                                      Model model)
    {
        model.addAttribute("bookmarksAndPage", userActivityFacade.getBookmarksAndPage(user.getId(), pageNumber));

        return FRAGMENT_BOOKMARK_LIST;
    }
}
