package store.babel.babel.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import store.babel.babel.domain.user.User;
import store.babel.babel.domain.user.controller.form.UserProfileForm;
import store.babel.babel.domain.user.service.UserProfileService;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.validator.UpdateProfileValidator;

import java.io.IOException;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserProfileController
{
    private final UpdateProfileValidator updateProfileValidator;

    @InitBinder("updateProfileForm")
    public void initBinder(WebDataBinder binder)
    {
        binder.addValidators(updateProfileValidator);
    }

    public static final String VIEWS_USER_PROFILE = "/views/user-profile/profile-update";
    private final UserProfileService profileService;

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
