package store.postHub.postHub.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.postHub.postHub.common.BaseEntity;
import store.postHub.postHub.domain.auth.service.command.SignUpCommand;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity
{
    private String profileImage;
    private String email;
    private String password;
    private String nickname;
    private String company;
    private Boolean admin;

    public static User from(SignUpCommand command, String hashedPassword)
    {
        return User.builder()
                .email(command.getEmail())
                .password(hashedPassword)
                .nickname(command.getNickname())
                .company(null)
                .admin(false)
                .build();
    }
}
