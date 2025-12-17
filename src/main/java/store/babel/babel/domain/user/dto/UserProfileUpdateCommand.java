package store.babel.babel.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.user.controller.dto.UserProfileUpdateRequest;

@Getter
@Builder
public class UserProfileUpdateCommand
{
    private Long userId;
    private String nickname;
    private String profileImage;

    public static UserProfileUpdateCommand from(UserProfileUpdateRequest request)
    {
        return UserProfileUpdateCommand.builder()
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .build();
    }
}
