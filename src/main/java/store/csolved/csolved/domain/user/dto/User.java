package store.csolved.csolved.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.csolved.csolved.domain.auth.service.command.SignUpCommand;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    private Long id;
    private String profileImage;
    private String email;
    private String password;
    private String nickname;
    private String company;
    private Boolean admin;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

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
