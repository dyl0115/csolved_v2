package store.csolved.csolved.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import store.csolved.csolved.global.utils.login.LoginRequestInterceptor;
import store.csolved.csolved.global.utils.login.LoginUserArgumentResolver;
import store.csolved.csolved.global.utils.filter.FilterRequestArgumentResovler;
import store.csolved.csolved.global.utils.page.PageRequestArgumentResolver;
import store.csolved.csolved.global.utils.search.SearchRequestArgumentResolver;
import store.csolved.csolved.global.utils.sort.SortRequestArgumentResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer
{
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final PageRequestArgumentResolver pageRequestArgumentResolver;
    private final SortRequestArgumentResolver sortRequestArgumentResolver;
    private final FilterRequestArgumentResovler filterRequestArgumentResovler;
    private final SearchRequestArgumentResolver searchRequestArgumentResolver;
    private final LoginRequestInterceptor loginRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // @LoginRequested가 있는 컨트롤러는 로그인 체크합니다.
        registry.addInterceptor(loginRequestInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers)
    {
        // @LoginUser가 있는 User타입 변수에 현재 로그인한 회원정보를 바인딩합니다.
        resolvers.add(loginUserArgumentResolver);

        // @PageInfo가 있는 Page타입 변수에 사용자가 요청한 page정보를 바인딩합니다.
        resolvers.add(pageRequestArgumentResolver);

        // @SortInfo가 있는 SortType 변수에 사용자가 요청한 sort정보를 바인딩합니다.
        resolvers.add(sortRequestArgumentResolver);

        // @FilterInfo가 있는 FilterRequest 변수에 사용자가 요청한 filter정보를 바인딩합니다.
        resolvers.add(filterRequestArgumentResovler);

        // @SearchInfo가 있는 SearchRequest 변수에 search정보를 바인딩합니다.
        resolvers.add(searchRequestArgumentResolver);
    }
}
