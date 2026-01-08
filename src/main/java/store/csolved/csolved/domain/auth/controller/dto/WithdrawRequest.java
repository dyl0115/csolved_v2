package store.csolved.csolved.domain.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawRequest
{
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
