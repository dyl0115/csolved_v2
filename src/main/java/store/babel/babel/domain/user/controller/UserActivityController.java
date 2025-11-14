package store.babel.babel.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.user.controller.view_model.BookmarkedCommunitiesWithPaginationResult;
import store.babel.babel.domain.user.controller.view_model.AnsweredCommunitiesWithPaginationResult;
import store.babel.babel.domain.user.controller.view_model.UserCommunitiesWithPaginationResult;
import store.babel.babel.domain.user.service.UserActivityService;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.domain.user.User;
import store.babel.babel.global.utils.page.PageInfo;

@RequestMapping("/users/activity")
@RequiredArgsConstructor
@Controller
public class UserActivityController
{
    public static final String VIEWS_USER_ACTIVITY = "/views/user-profile/activity";
    public static final String FRAGMENT_REPLIED_POST_LIST = "/views/user-profile/activity :: repliedPostList";
    public static final String FRAGMENT_USER_POST_LIST = "/views/user-profile/activity :: userPostList";
    private static final String FRAGMENT_BOOKMARK_LIST = "/views/user-profile/activity :: bookmarkList";

    private final UserActivityService userActivityService;

    @LoginRequest
    @GetMapping
    public String getUserActivities(@LoginUser User user,
                                    @PageInfo(type = "bookmarkPage") Long bookmarkPageNumber,
                                    @PageInfo(type = "repliedPostPage") Long repliedPostPageNumber,
                                    @PageInfo(type = "userPostPage") Long userPostPageNumber,
                                    Model model)
    {
        BookmarkedCommunitiesWithPaginationResult bookmarksAndPage = userActivityService.getBookmarkedCommunitiesWithPagination(user.getId(), bookmarkPageNumber);
        model.addAttribute("bookmarksAndPage", bookmarksAndPage);

        AnsweredCommunitiesWithPaginationResult repliedPostsAndPage = userActivityService.getAnsweredCommunitiesWithPagination(user.getId(), repliedPostPageNumber);
        model.addAttribute("repliedPostsAndPage", repliedPostsAndPage);

        UserCommunitiesWithPaginationResult userPostsAndPage = userActivityService.getUserPostsAndPage(user.getId(), userPostPageNumber);
        model.addAttribute("userPostsAndPage", userPostsAndPage);

        return VIEWS_USER_ACTIVITY;
    }

    @LoginRequest
    @GetMapping("/bookmarkedPosts")
    public String getBookmarksAndPage(@LoginUser User user,
                                      @PageInfo(type = "bookmarkPage") Long pageNumber,
                                      Model model)
    {
        BookmarkedCommunitiesWithPaginationResult bookmarkedCommunitiesWithPagination = userActivityService.getBookmarkedCommunitiesWithPagination(user.getId(), pageNumber);
        model.addAttribute("bookmarksAndPage", bookmarkedCommunitiesWithPagination);

        return FRAGMENT_BOOKMARK_LIST;
    }


    @LoginRequest
    @GetMapping("/repliedPosts")
    public String getRepliedPost(@LoginUser User user,
                                 @PageInfo(type = "repliedPostPage") Long repliedPostPageNumber,
                                 Model model)
    {
        AnsweredCommunitiesWithPaginationResult repliedPostsAndPage = userActivityService.getAnsweredCommunitiesWithPagination(user.getId(), repliedPostPageNumber);
        model.addAttribute("repliedPostsAndPage", repliedPostsAndPage);

        return FRAGMENT_REPLIED_POST_LIST;
    }

    @LoginRequest
    @GetMapping("/userPosts")
    public String getUserPosts(@LoginUser User user,
                               @PageInfo(type = "userPostPage") Long userPostPageNumber,
                               Model model)
    {
        UserCommunitiesWithPaginationResult userPostsAndPage = userActivityService.getUserPostsAndPage(user.getId(), userPostPageNumber);
        model.addAttribute("userPostsAndPage", userPostsAndPage);

        return FRAGMENT_USER_POST_LIST;
    }
}