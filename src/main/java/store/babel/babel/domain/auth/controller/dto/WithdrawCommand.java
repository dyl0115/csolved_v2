package store.babel.babel.domain.auth.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawCommand
{
    private String password;

    public static WithdrawCommand from(WithdrawRequest request)
    {
        return WithdrawCommand.builder()
                .password(request.getPassword())
                .build();
    }
}
