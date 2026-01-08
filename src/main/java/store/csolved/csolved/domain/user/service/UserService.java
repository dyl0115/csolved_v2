package store.csolved.csolved.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.domain.user.mapper.UserMapper;

@RequiredArgsConstructor
@Service
public class UserService
{
    private final UserMapper userMapper;

    public User getUser(Long userId)
    {
        return userMapper.findUserById(userId);
    }

    @Transactional
    public void updateProfile(Long userId, String profileImage)
    {
        userMapper.updateProfileImage(userId, profileImage);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname)
    {
        userMapper.updateNickname(userId, nickname);
    }
}
