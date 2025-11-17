package store.babel.babel.domain.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest
{
    private Long userId;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 8, message = "길이가 2에서 8 사이여야 합니다.")
    private String nickname;

    // Base64로 인코딩된 이미지 데이터
    private String profileImageBase64;

    // 이미지 파일명
    private String profileImageFileName;

    // 현재 프로필 이미지 경로
    private String currentProfileImage;
}
