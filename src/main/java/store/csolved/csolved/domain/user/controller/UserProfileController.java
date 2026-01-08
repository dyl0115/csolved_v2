package store.csolved.csolved.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.user.controller.dto.UserProfileUpdateRequest;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.domain.user.dto.UserProfile;
import store.csolved.csolved.domain.user.dto.UserProfileUpdateCommand;
import store.csolved.csolved.domain.user.service.UserProfileService;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;

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
