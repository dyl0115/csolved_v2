package store.babel.babel.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.user.controller.dto.UserProfileUpdateRequest;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.domain.user.dto.UserProfile;
import store.babel.babel.domain.user.dto.UserProfileUpdateCommand;
import store.babel.babel.domain.user.service.UserProfileService;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserProfileController
{
    public static final String VIEWS_USER_PROFILE = "/views/user/profile";
    private final UserProfileService userProfileService;

    @LoginRequest
    @GetMapping("/profile")
    public String initUpdateProfile(@LoginUser User user,
                                    Model model)
    {
        model.addAttribute("userProfile", UserProfile.from(user));
        return VIEWS_USER_PROFILE;
    }

    @LoginRequest
    @PutMapping("profile")
    @ResponseBody
    public void updateProfile(@LoginUser User user,
                              @Valid @RequestBody UserProfileUpdateRequest request)
    {
        userProfileService.updateProfile(user, UserProfileUpdateCommand.from(request));
    }
}
