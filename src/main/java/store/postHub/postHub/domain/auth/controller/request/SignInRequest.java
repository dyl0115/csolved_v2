package store.postHub.postHub.domain.auth.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class SignInRequest
{
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
