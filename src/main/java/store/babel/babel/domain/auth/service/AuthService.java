package store.babel.babel.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.auth.service.command.SignInCommand;
import store.babel.babel.domain.auth.service.command.SignUpCommand;
import store.babel.babel.domain.user.User;
import store.babel.babel.domain.user.mapper.UserMapper;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.utils.PasswordManager;
import store.babel.babel.global.utils.AuthSessionManager;

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
            throw new BabelException(ExceptionCode.DUPLICATE_EMAIL);
        }

        if (userMapper.existsByNickname(command.getNickname()))
        {
            throw new BabelException(ExceptionCode.DUPLICATE_NICKNAME);
        }

        if (!Objects.equals(command.getPassword(), command.getPasswordConfirm()))
        {
            throw new BabelException(ExceptionCode.PASSWORD_MISMATCH);
        }


        String hashedPassword = passwordManager.hashPassword(command.getPassword());
        userMapper.insertUser(User.from(command, hashedPassword));
    }

    public void signIn(SignInCommand command)
    {
        User user = userMapper.findUserByEmail(command.getEmail());

        if (user == null)
        {
            throw new BabelException(ExceptionCode.USER_NOT_FOUND);
        }

        String storedPassword = userMapper.findPasswordByEmail(command.getEmail());

        if (storedPassword == null || !passwordManager.verifyPassword(command.getPassword(), storedPassword))
        {
            throw new BabelException(ExceptionCode.INVALID_PASSWORD);
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
