package store.csolved.csolved.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.domain.user.controller.form.UserProfileForm;
import store.csolved.csolved.exception.ImageUploadException;
import store.csolved.csolved.domain.file.FileService;
import store.csolved.csolved.utils.AuthSessionManager;

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
            String profileUrl = s3Service.upload(profileImage, FOLDER_NAME_USER_PROFILE);
            if (profileUrl == null)
            {
                throw new ImageUploadException("이미지 저장 실패");
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
}