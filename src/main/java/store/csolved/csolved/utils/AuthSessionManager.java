package store.csolved.csolved.utils;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.csolved.csolved.domain.user.User;

@RequiredArgsConstructor
@Component
public class AuthSessionManager
{
    private final static String LOGIN_USER_SESSION_KEY = "loginUser";
    private final HttpSession httpSession;

    public void setLoginUser(User user)
    {
        httpSession.setAttribute(LOGIN_USER_SESSION_KEY, user);
    }

    public User getLoginUser()
    {
        return (User) httpSession.getAttribute(LOGIN_USER_SESSION_KEY);
    }

    public void invalidateSession()
    {
        httpSession.invalidate();
    }
}
