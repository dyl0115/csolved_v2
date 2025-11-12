package store.csolved.csolved.global.utils.search;

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
public class SearchRequestArgumentResolver implements HandlerMethodArgumentResolver
{
    private final static Map<String, SearchType> SEARCH_TYPE_MAP = new HashMap<>();
    private final static String SEARCH_TYPE_PARAMETER_NAME = "searchType";
    private final static String SEARCH_VALUE_PARAMETER_NAME = "searchKeyword";

    @PostConstruct
    public void init()
    {
        Arrays.stream(SearchType.values())
                .forEach(e -> SEARCH_TYPE_MAP.put(e.name().toLowerCase(), e));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        boolean hasSearchInfoAnnotation = parameter.hasParameterAnnotation(SearchInfo.class);
        boolean isSearchRequestType = parameter.getParameterType().equals(Searching.class);
        return hasSearchInfoAnnotation && isSearchRequestType;
    }

    @Override
    public Searching resolveArgument(MethodParameter parameter,
                                     ModelAndViewContainer mavContainer,
                                     NativeWebRequest webRequest,
                                     WebDataBinderFactory binderFactory)
    {
        String requestedSearchType = webRequest.getParameter(SEARCH_TYPE_PARAMETER_NAME);
        SearchType searchType = SEARCH_TYPE_MAP.getOrDefault(requestedSearchType, SearchType.NONE);

        String searchValue = webRequest.getParameter(SEARCH_VALUE_PARAMETER_NAME);
        return Searching.create(searchType.name(), searchValue);
    }
}
