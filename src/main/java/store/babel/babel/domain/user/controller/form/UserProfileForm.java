package store.babel.babel.domain.user.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import store.babel.babel.domain.user.dto.User;

@Getter
@Builder
public class UserProfileForm
{
    private Long userId;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 8, message = "길이가 2에서 8 사이여야 합니다.")
    private String nickname;
    private String currentProfileImage;
    private MultipartFile profileImage;

    public static UserProfileForm from(User user)
    {
        return UserProfileForm.builder()
                .nickname(user.getNickname())
                .currentProfileImage(user.getProfileImage())
                .build();
    }

    public void bindCurrentProfileImage(String currentProfileImage)
    {
        this.currentProfileImage = currentProfileImage;
    }
}
