package store.csolved.csolved.domain.assistant.session;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginUserArgumentResolver;

@RequiredArgsConstructor
@Component
public class ChatSessionArgumentResolver implements HandlerMethodArgumentResolver
{
    private final AssistantChatSessionManager sessionManager;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        return parameter.hasParameterAnnotation(ChatSession.class)
                && parameter.getParameterType().equals(AssistantChatSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception
    {
        User user = loginUserArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return sessionManager.getSession(user.getId());
    }
}
