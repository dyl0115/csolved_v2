package store.postHub.postHub.global.utils.filter;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class FilterRequestArgumentResovler implements HandlerMethodArgumentResolver
{
    public final static String FILTER_TYPE_PARAMETER_NAME = "filterType";
    public final static String FILTER_VALUE_PARAMETER_NAME = "filterValue";

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        boolean hasFilterInfoAnnotation = parameter.hasParameterAnnotation(FilterInfo.class);
        boolean isFilterRequestType = parameter.getParameterType().equals(Filtering.class);
        return hasFilterInfoAnnotation && isFilterRequestType;
    }

    @Override
    public Filtering resolveArgument(MethodParameter parameter,
                                     ModelAndViewContainer mavContainer,
                                     NativeWebRequest webRequest,
                                     WebDataBinderFactory binderFactory)
    {
        String filterType = webRequest.getParameter(FILTER_TYPE_PARAMETER_NAME);
        String filterValue = webRequest.getParameter(FILTER_VALUE_PARAMETER_NAME);

        if (filterValue == null) return Filtering.create(filterType, 0L);
        return Filtering.create(filterType, Long.parseLong(filterValue));
    }
}
