package store.babel.babel.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.domain.user.controller.dto.UserProfileUpdateRequest;
import store.babel.babel.domain.user.controller.form.UserProfileForm;
import store.babel.babel.domain.file.service.FileService;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.utils.AuthSessionManager;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UserProfileService
{
    private final static String FOLDER_NAME_USER_PROFILE = "user";

    private final AuthSessionManager sessionManager;
    private final FileService s3Service;
    private final UserService userService;

    public void updateProfile(UserProfileForm form) throws IOException
    {
        MultipartFile profileImage = form.getProfileImage();

        if (!profileImage.isEmpty())
        {
            String profileUrl = s3Service.uploadImage(profileImage);

            if (profileUrl == null)
            {
                throw new BabelException(ExceptionCode.IMAGE_UPLOAD_FAILED);
            }

            userService.updateProfile(form.getUserId(), profileUrl);
        }
        userService.updateNickname(form.getUserId(), form.getNickname());
        User user = userService.getUser(form.getUserId());
        form.bindCurrentProfileImage(user.getProfileImage());
        sessionManager.setLoginUser(user);
    }

    public void restoreProfile(UserProfileForm form)
    {
        User user = userService.getUser(form.getUserId());
        form.bindCurrentProfileImage(user.getProfileImage());
    }

    /**
     * Base64 이미지 데이터를 사용하여 프로필을 업데이트합니다.
     * @param request UserProfileUpdateRequest (Base64 이미지 포함)
     * @return 업데이트된 User 객체
     */
    public User updateProfileWithBase64(UserProfileUpdateRequest request) throws IOException
    {
        String profileImageBase64 = request.getProfileImageBase64();

        // Base64 이미지가 있으면 업로드
        if (profileImageBase64 != null && !profileImageBase64.isEmpty())
        {
            String fileName = request.getProfileImageFileName();
            if (fileName == null || fileName.isEmpty())
            {
                fileName = "profile_" + System.currentTimeMillis() + ".jpg";
            }

            String profileUrl = s3Service.uploadBase64Image(profileImageBase64, fileName);

            if (profileUrl == null)
            {
                throw new BabelException(ExceptionCode.IMAGE_UPLOAD_FAILED);
            }

            userService.updateProfile(request.getUserId(), profileUrl);
        }

        // 닉네임 업데이트
        userService.updateNickname(request.getUserId(), request.getNickname());

        // 업데이트된 사용자 정보 가져오기 및 세션 갱신
        User user = userService.getUser(request.getUserId());
        sessionManager.setLoginUser(user);

        return user;
    }
}