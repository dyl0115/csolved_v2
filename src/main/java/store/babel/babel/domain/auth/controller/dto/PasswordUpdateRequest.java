package store.babel.babel.domain.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordUpdateRequest
{
    private String currentPassword;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()]{8,20}$",
            message = "비밀번호는 8~20자의 영문자, 숫자 조합이어야 합니다. 특수문자(!@#$%^&*())도 사용 가능합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String newPasswordConfirm;
}
