package store.babel.babel.domain.auth.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse
{
    private Boolean success;
    private String message;

    public static SignUpResponse success()
    {
        return SignUpResponse.builder()
                .success(true)
                .message("회원가입이 완료되었습니다.")
                .build();
    }
}
