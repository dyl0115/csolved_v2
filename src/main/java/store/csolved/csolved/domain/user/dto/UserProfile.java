package store.csolved.csolved.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfile
{
    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;

    public static UserProfile from(User user)
    {
        return UserProfile.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
