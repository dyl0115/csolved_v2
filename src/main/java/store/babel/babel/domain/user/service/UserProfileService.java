package store.babel.babel.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import store.babel.babel.domain.user.User;
import store.babel.babel.domain.user.controller.form.UserProfileForm;
import store.babel.babel.domain.file.service.FileService;
import store.babel.babel.global.exception.CsolvedException;
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
                throw new CsolvedException(ExceptionCode.IMAGE_UPLOAD_FAILED);
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