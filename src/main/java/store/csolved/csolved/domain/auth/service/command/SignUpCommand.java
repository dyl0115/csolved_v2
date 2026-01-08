package store.csolved.csolved.domain.auth.service.command;

import lombok.Builder;
import lombok.Data;
import store.csolved.csolved.domain.auth.controller.dto.SignUpRequest;

@Data
@Builder
public class SignUpCommand
{
    String email;
    String nickname;
    String password;
    String passwordConfirm;

    public static SignUpCommand from(SignUpRequest request)
    {
        return SignUpCommand.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .passwordConfirm(request.getPasswordConfirm())
                .build();
    }
}
