package store.babel.babel.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import store.babel.babel.domain.user.User;
import store.babel.babel.domain.user.controller.dto.UserProfileUpdateRequest;
import store.babel.babel.domain.user.controller.form.UserProfileForm;
import store.babel.babel.domain.user.service.UserProfileService;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/users")
@RequiredArgsConstructor
@Controller
public class UserProfileController
{

    public static final String VIEWS_USER_PROFILE = "/views/user/profile";
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

    /**
     * REST API 엔드포인트: fetch로 호출되는 프로필 업데이트
     * @RequestBody로 JSON 데이터를 받습니다
     */
    @LoginRequest
    @PostMapping("/profile/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfileApi(
            @Valid @RequestBody UserProfileUpdateRequest request,
            BindingResult result,
            @LoginUser User user) throws IOException
    {
        Map<String, Object> response = new HashMap<>();

        // 유효성 검증 오류가 있는 경우
        if (result.hasErrors())
        {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            response.put("success", false);
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }

        // 요청에 userId가 없으면 현재 로그인한 사용자의 ID 사용
        if (request.getUserId() == null)
        {
            request.setUserId(user.getId());
        }

        try
        {
            User updatedUser = profileService.updateProfileWithBase64(request);
            response.put("success", true);
            response.put("message", "프로필이 성공적으로 업데이트되었습니다.");
            response.put("profileImage", updatedUser.getProfileImage());
            response.put("nickname", updatedUser.getNickname());
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            response.put("success", false);
            response.put("message", "프로필 업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
