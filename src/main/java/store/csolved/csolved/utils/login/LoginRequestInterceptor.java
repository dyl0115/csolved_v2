package store.csolved.csolved.utils.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.utils.AuthSessionManager;


@RequiredArgsConstructor
@Component
public class LoginRequestInterceptor implements HandlerInterceptor
{
    private final AuthSessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // 핸들러매핑에서 매칭되는 컨트롤러가 없는 경우에는 그대로 진행해야 한다. ex) 404예외
        if (!(handler instanceof HandlerMethod handlerMethod)) return true;

        // 핸들러매핑에서 매칭되는 컨트롤러에 @LoginRequest가 있는지 확인
        boolean hasRequestLoginAnnotation = handlerMethod
                .hasMethodAnnotation(LoginRequest.class);

        // 컨트롤러 메서드에 @RequestLogin이 없으면 그대로 진행
        if (!hasRequestLoginAnnotation) return true;

        // 컨트롤러 메서드에 @RequestLogin이 있고, 로그아웃 상태라면 로그인 화면으로 리다이렉트
        User user = sessionManager.getLoginUser();
        if (user == null)
        {
            response.sendRedirect("/auth/signIn");
            return false;
        }

        // 컨트롤러 메서드에 @RequestLogin이 있고, 로그인 상태라면 그대로 진행
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception
    {
        // modelAndView가 null인 경우, 즉 api 요청인 경우 아무런 데이터도 넘겨주지 않고 지나간다.

        // modelAndView가 null이 아닌 경우, View로 loginUser 데이터를 넘겨준다.
        if (modelAndView != null)
        {
            User loginUser = sessionManager.getLoginUser();

            // 만약 컨트롤러에서 user가 업데이트가 되어 modelAndView에 담겼다면,
            // 업데이트 된 user 정보를 넘겨준다.
            if (modelAndView.getModel().get("user") != null)
            {
                loginUser = (User) modelAndView.getModel().get("user");
            }

            // View로 user 정보를 넘겨준다.
            modelAndView.addObject("user", loginUser);
        }
    }
}
