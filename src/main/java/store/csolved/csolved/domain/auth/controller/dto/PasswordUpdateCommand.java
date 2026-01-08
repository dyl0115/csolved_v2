package store.csolved.csolved.domain.auth.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordUpdateCommand
{
    String currentPassword;
    String newPassword;
    String newPasswordConfirm;

    public static PasswordUpdateCommand from(PasswordUpdateRequest request)
    {
        return PasswordUpdateCommand.builder()
                .currentPassword(request.getCurrentPassword())
                .newPassword(request.getNewPassword())
                .newPasswordConfirm(request.getNewPasswordConfirm())
                .build();
    }
}
