package store.postHub.postHub.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import store.postHub.postHub.domain.user.controller.view_model.BookmarksAndPageVM;
import store.postHub.postHub.domain.user.controller.view_model.RepliedPostsAndPageVM;
import store.postHub.postHub.domain.user.controller.view_model.UserPostsAndPageVM;
import store.postHub.postHub.domain.user.service.UserActivityFacade;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.global.utils.login.LoginUser;
import store.postHub.postHub.domain.user.User;
import store.postHub.postHub.domain.user.controller.form.UserProfileForm;
import store.postHub.postHub.domain.user.service.UserProfileService;
import store.postHub.postHub.global.utils.page.PageInfo;
import store.postHub.postHub.validator.UpdateProfileValidator;

import java.io.IOException;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserController
{
    public static final String VIEWS_USER_PROFILE = "/views/user-profile/profile-update";
    public static final String VIEWS_USER_ACTIVITY = "/views/user-profile/activity";

    public static final String FRAGMENT_REPLIED_POST_LIST = "/views/user-profile/activity :: repliedPostList";
    public static final String FRAGMENT_USER_POST_LIST = "/views/user-profile/activity :: userPostList";

    private final UserProfileService profileService;
    private final UserActivityFacade userActivityFacade;
    private final UpdateProfileValidator updateProfileValidator;

    @InitBinder("updateProfileForm")
    public void initBinder(WebDataBinder binder)
    {
        binder.addValidators(updateProfileValidator);
    }

    @LoginRequest
    @GetMapping("/activity")
    public String getUserActivities(@LoginUser User user,
                                    @PageInfo(type = "bookmarkPage") Long bookmarkPageNumber,
                                    @PageInfo(type = "repliedPostPage") Long repliedPostPageNumber,
                                    @PageInfo(type = "userPostPage") Long userPostPageNumber,
                                    Model model)
    {
        BookmarksAndPageVM bookmarksAndPage = userActivityFacade.getBookmarksAndPage(user.getId(), bookmarkPageNumber);
        model.addAttribute("bookmarksAndPage", bookmarksAndPage);

        RepliedPostsAndPageVM repliedPostsAndPage = userActivityFacade.getRepliedPostsAndPage(user.getId(), repliedPostPageNumber);
        model.addAttribute("repliedPostsAndPage", repliedPostsAndPage);

        UserPostsAndPageVM userPostsAndPage = userActivityFacade.getUserPostsAndPage(user.getId(), userPostPageNumber);
        model.addAttribute("userPostsAndPage", userPostsAndPage);

        return VIEWS_USER_ACTIVITY;
    }

    @LoginRequest
    @GetMapping("/activity/repliedPosts")
    public String getRepliedPost(@LoginUser User user,
                                 @PageInfo(type = "repliedPostPage") Long repliedPostPageNumber,
                                 Model model)
    {
        RepliedPostsAndPageVM repliedPostsAndPage = userActivityFacade.getRepliedPostsAndPage(user.getId(), repliedPostPageNumber);
        model.addAttribute("repliedPostsAndPage", repliedPostsAndPage);

        return FRAGMENT_REPLIED_POST_LIST;
    }

    @LoginRequest
    @GetMapping("/activity/userPosts")
    public String getUserPosts(@LoginUser User user,
                               @PageInfo(type = "userPostPage") Long userPostPageNumber,
                               Model model)
    {
        UserPostsAndPageVM userPostsAndPage = userActivityFacade.getUserPostsAndPage(user.getId(), userPostPageNumber);
        model.addAttribute("userPostsAndPage", userPostsAndPage);

        return FRAGMENT_USER_POST_LIST;
    }

    @LoginRequest
    @GetMapping("/profile")
    public String initUpdateProfile(@LoginUser User user,
                                    Model model)
    {
        model.addAttribute("updateProfileForm", UserProfileForm.from(user));
        return VIEWS_USER_PROFILE;
    }

    @LoginRequest
    @PostMapping("/profile")
    public String getUser(@Valid @ModelAttribute("updateProfileForm") UserProfileForm form,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) throws IOException
    {
        if (result.hasErrors())
        {
            profileService.restoreProfile(form);
            return VIEWS_USER_PROFILE;
        }

        profileService.updateProfile(form);
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/users/profile";
    }
}