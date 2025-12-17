package store.babel.babel.domain.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawRequest
{
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
