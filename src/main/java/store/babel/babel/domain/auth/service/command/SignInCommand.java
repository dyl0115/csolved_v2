package store.babel.babel.domain.auth.service.command;

import lombok.Builder;
import lombok.Data;
import store.babel.babel.domain.auth.controller.dto.SignInRequest;

@Data
@Builder
public class SignInCommand
{
    String email;
    String password;

    public static SignInCommand from(SignInRequest request)
    {
        return SignInCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
