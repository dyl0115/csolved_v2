package store.csolved.csolved.global.utils.sort;

import jakarta.annotation.PostConstruct;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class SortRequestArgumentResolver implements HandlerMethodArgumentResolver
{
    private final static Map<String, Sorting> SORT_TYPE_MAP = new HashMap<>();
    private final static String SORT_PARAMETER_NAME = "sortType";

    @PostConstruct
    public void init()
    {
        Arrays.stream(Sorting.values())
                .forEach(e -> SORT_TYPE_MAP.put(e.name().toLowerCase(), e));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        boolean hasSortInfoAnnotation = parameter.hasParameterAnnotation(SortInfo.class);
        boolean isSortType = parameter.getParameterType().equals(Sorting.class);
        return hasSortInfoAnnotation && isSortType;
    }

    @Override
    public Sorting resolveArgument(MethodParameter parameter,
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory)
    {
        String sortString = webRequest.getParameter(SORT_PARAMETER_NAME);
        if (sortString == null) return Sorting.RECENT;
        return SORT_TYPE_MAP.getOrDefault(sortString, Sorting.RECENT);
    }
}
