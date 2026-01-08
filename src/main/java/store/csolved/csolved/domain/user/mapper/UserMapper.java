package store.csolved.csolved.domain.user.mapper;

import org.apache.ibatis.annotations.*;
import store.csolved.csolved.domain.user.dto.User;

@Mapper
public interface UserMapper
{
    void insertUser(User user);

    User findUserById(Long id);

    User findUserByEmail(String email);

    String findPasswordById(Long id);

    String findPasswordByEmail(String Email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    void updateProfileImage(Long userId, String profileImage);

    void updateNickname(Long userId, String nickname);

    void updatePassword(Long userId, String password);

    void delete(Long id);
}
