package store.babel.babel.domain.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.user.dto.User;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class UserMapperTest
{
    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("사용자를 저장하고 ID가 자동 생성되는지 확인")
    void insertUser()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        // when
        userMapper.insertUser(user);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("ID로 사용자를 조회")
    void findUserById()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        User foundUser = userMapper.findUserById(user.getId());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getNickname()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("이메일로 사용자를 조회")
    void findUserByEmail()
    {
        // given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        User foundUser = userMapper.findUserByEmail(email);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(email);
        assertThat(foundUser.getNickname()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("이메일로 비밀번호를 조회")
    void findPasswordByEmail()
    {
        // given
        String email = "test@example.com";
        String password = "password123";
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        String foundPassword = userMapper.findPasswordByEmail(email);

        // then
        assertThat(foundPassword).isNotNull();
        assertThat(foundPassword).isEqualTo(password);
    }

    @Test
    @DisplayName("존재하는 이메일은 true 반환")
    void existsByEmail_whenExists()
    {
        // given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        boolean exists = userMapper.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 이메일은 false 반환")
    void existsByEmail_whenNotExists()
    {
        // given
        String email = "nonexistent@example.com";

        // when
        boolean exists = userMapper.existsByEmail(email);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("존재하는 닉네임은 true 반환")
    void existsByNickname_whenExists()
    {
        // given
        String nickname = "testUser";
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname(nickname)
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        boolean exists = userMapper.existsByNickname(nickname);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 닉네임은 false 반환")
    void existsByNickname_whenNotExists()
    {
        // given
        String nickname = "nonexistentUser";

        // when
        boolean exists = userMapper.existsByNickname(nickname);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("프로필 이미지를 업데이트하고 확인")
    void updateProfileImage()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        String newProfileImage = "https://example.com/profile.jpg";

        // when
        userMapper.updateProfileImage(user.getId(), newProfileImage);

        // then
        User updatedUser = userMapper.findUserById(user.getId());
        assertThat(updatedUser.getProfileImage()).isEqualTo(newProfileImage);
        assertThat(updatedUser.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("닉네임을 업데이트하고 확인")
    void updateNickname()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("oldNickname")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        String newNickname = "newNickname";

        // when
        userMapper.updateNickname(user.getId(), newNickname);

        // then
        User updatedUser = userMapper.findUserById(user.getId());
        assertThat(updatedUser.getNickname()).isEqualTo(newNickname);
        assertThat(updatedUser.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("사용자를 논리적으로 삭제하고 조회되지 않는지 확인")
    void delete()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        userMapper.delete(user.getId());

        // then
        User deletedUser = userMapper.findUserById(user.getId());
        assertThat(deletedUser).isNull();
    }

    @Test
    @DisplayName("삭제된 사용자는 이메일로 조회되지 않음")
    void findUserByEmail_afterDelete()
    {
        // given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);
        userMapper.delete(user.getId());

        // when
        User deletedUser = userMapper.findUserByEmail(email);

        // then
        assertThat(deletedUser).isNull();
    }

    @Test
    @DisplayName("삭제된 사용자의 닉네임은 존재하지 않음으로 처리")
    void existsByNickname_afterDelete()
    {
        // given
        String nickname = "testUser";
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname(nickname)
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);
        userMapper.delete(user.getId());

        // when
        boolean exists = userMapper.existsByNickname(nickname);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("삭제된 사용자의 이메일은 여전히 존재함으로 처리")
    void existsByEmail_afterDelete()
    {
        // given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);
        userMapper.delete(user.getId());

        // when
        boolean exists = userMapper.existsByEmail(email);

        // then
        // existsByEmail은 deleted_at 체크가 없으므로 true 반환
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("admin 권한을 가진 사용자를 생성하고 조회")
    void insertAdminUser()
    {
        // given
        User adminUser = User.builder()
                .email("admin@example.com")
                .password("adminpassword")
                .nickname("adminUser")
                .company("testCompany")
                .admin(true)
                .build();

        // when
        userMapper.insertUser(adminUser);

        // then
        User foundUser = userMapper.findUserById(adminUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getAdmin()).isTrue();
    }

    @Test
    @DisplayName("company가 null인 사용자를 생성하고 조회")
    void insertUserWithNullCompany()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                .company(null)
                .admin(false)
                .build();

        // when
        userMapper.insertUser(user);

        // then
        User foundUser = userMapper.findUserById(user.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getCompany()).isNull();
    }

    @Test
    @DisplayName("여러 사용자를 생성하고 각각 조회")
    void insertMultipleUsers()
    {
        // given
        User user1 = User.builder()
                .email("user1@example.com")
                .password("password1")
                .nickname("user1")
                .company("company1")
                .admin(false)
                .build();

        User user2 = User.builder()
                .email("user2@example.com")
                .password("password2")
                .nickname("user2")
                .company("company2")
                .admin(false)
                .build();

        User user3 = User.builder()
                .email("user3@example.com")
                .password("password3")
                .nickname("user3")
                .company("company3")
                .admin(true)
                .build();

        // when
        userMapper.insertUser(user1);
        userMapper.insertUser(user2);
        userMapper.insertUser(user3);

        // then
        User foundUser1 = userMapper.findUserByEmail("user1@example.com");
        User foundUser2 = userMapper.findUserByEmail("user2@example.com");
        User foundUser3 = userMapper.findUserByEmail("user3@example.com");

        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser3).isNotNull();
        assertThat(foundUser1.getNickname()).isEqualTo("user1");
        assertThat(foundUser2.getNickname()).isEqualTo("user2");
        assertThat(foundUser3.getNickname()).isEqualTo("user3");
        assertThat(foundUser3.getAdmin()).isTrue();
    }

    @Test
    @DisplayName("프로필 이미지를 여러 번 업데이트")
    void updateProfileImageMultipleTimes()
    {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                .company("testCompany")
                .admin(false)
                .build();

        userMapper.insertUser(user);

        // when
        userMapper.updateProfileImage(user.getId(), "https://example.com/profile1.jpg");
        User afterFirstUpdate = userMapper.findUserById(user.getId());

        userMapper.updateProfileImage(user.getId(), "https://example.com/profile2.jpg");
        User afterSecondUpdate = userMapper.findUserById(user.getId());

        // then
        assertThat(afterFirstUpdate.getProfileImage()).isEqualTo("https://example.com/profile1.jpg");
        assertThat(afterSecondUpdate.getProfileImage()).isEqualTo("https://example.com/profile2.jpg");
    }
}
