package store.csolved.csolved.domain.auth.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignOutResponse
{
    private Boolean success;
    private String message;

    public static SignOutResponse success()
    {
        return SignOutResponse.builder()
                .success(true)
                .message("로그아웃을 완료했습니다.")
                .build();
    }
}
