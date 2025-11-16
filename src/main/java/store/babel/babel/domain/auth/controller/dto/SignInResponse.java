package store.babel.babel.domain.auth.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponse
{
    private Boolean success;
    private String message;

    public static SignInResponse success()
    {
        return SignInResponse.builder()
                .success(true)
                .message("로그인에 성공했습니다.")
                .build();
    }
}
