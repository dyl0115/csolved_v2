package store.postHub.postHub.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import store.postHub.postHub.domain.auth.service.command.SignInCommand;
import store.postHub.postHub.domain.auth.service.command.SignUpCommand;
import store.postHub.postHub.domain.user.User;
import store.postHub.postHub.domain.user.mapper.UserMapper;
import store.postHub.postHub.global.exception.CsolvedException;
import store.postHub.postHub.global.exception.ExceptionCode;
import store.postHub.postHub.global.utils.PasswordManager;
import store.postHub.postHub.global.utils.AuthSessionManager;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class AuthService
{
    private final AuthSessionManager authSessionManager;
    private final PasswordManager passwordManager;
    private final UserMapper userMapper;

    @Transactional
    public void signUp(SignUpCommand command)
    {
        if (userMapper.existsByEmail(command.getEmail()))
        {
            throw new CsolvedException(ExceptionCode.DUPLICATE_EMAIL);
        }

        if (userMapper.existsByNickname(command.getNickname()))
        {
            throw new CsolvedException(ExceptionCode.DUPLICATE_NICKNAME);
        }

        if (!Objects.equals(command.getPassword(), command.getPasswordConfirm()))
        {
            throw new CsolvedException(ExceptionCode.PASSWORD_MISMATCH);
        }


        String hashedPassword = passwordManager.hashPassword(command.getPassword());
        userMapper.insertUser(User.from(command, hashedPassword));
    }

    public void signIn(SignInCommand command)
    {
        User user = userMapper.findUserByEmail(command.getEmail());

        if (user == null)
        {
            throw new CsolvedException(ExceptionCode.USER_NOT_FOUND);
        }

        String storedPassword = userMapper.findPasswordByEmail(command.getEmail());

        if (storedPassword == null || !passwordManager.verifyPassword(command.getPassword(), storedPassword))
        {
            throw new CsolvedException(ExceptionCode.INVALID_PASSWORD);
        }

        authSessionManager.setLoginUser(user);
    }

    public void signOut()
    {
        authSessionManager.invalidateSession();
    }

    @Transactional
    public void withdraw(User user)
    {
        authSessionManager.invalidateSession();
        userMapper.delete(user.getId());
    }
}
