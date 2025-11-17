package store.babel.babel.domain.user.mapper;

import org.apache.ibatis.annotations.*;
import store.babel.babel.domain.user.dto.User;

@Mapper
public interface UserMapper
{
    void insertUser(User user);

    User findUserById(Long id);

    User findUserByEmail(String email);

    String findPasswordByEmail(String Email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    void updateProfileImage(Long userId, String profileImage);

    void updateNickname(Long userId, String nickname);

    void delete(Long id);
}
