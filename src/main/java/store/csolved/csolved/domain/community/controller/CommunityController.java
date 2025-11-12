package store.csolved.csolved.domain.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.category.Category;
import store.csolved.csolved.domain.category.service.CategoryService;
import store.csolved.csolved.domain.community.service.CommunityService;
import store.csolved.csolved.domain.community.service.result.CommunitiesAndPageResult;
import store.csolved.csolved.domain.community.service.result.CommunityResult;
import store.csolved.csolved.domain.community.service.result.CommunityWithAnswersAndCommentsResult;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.filter.FilterInfo;
import store.csolved.csolved.global.utils.filter.Filtering;
import store.csolved.csolved.global.utils.login.LoginUser;
import store.csolved.csolved.global.utils.page.PageInfo;
import store.csolved.csolved.global.utils.search.SearchInfo;
import store.csolved.csolved.global.utils.search.Searching;
import store.csolved.csolved.global.utils.sort.SortInfo;
import store.csolved.csolved.global.utils.sort.Sorting;

import java.util.List;

import static store.csolved.csolved.common.PostType.COMMUNITY;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommunityController
{
    public final static String VIEWS_COMMUNITY_CREATE_FORM = "/views/community/create";
    public final static String VIEWS_COMMUNITY_UPDATE_FORM = "/views/community/update";
    public final static String VIEWS_COMMUNITY_LIST = "/views/community/list";
    public final static String VIEWS_COMMUNITY_DETAIL = "/views/community/detail";

    private final CommunityService communityService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/communities")
    public String getPosts(@PageInfo Long pageNumber,
                           @SortInfo Sorting sort,
                           @FilterInfo Filtering filter,
                           @SearchInfo Searching search,
                           Model model)
    {
        CommunitiesAndPageResult communitiesAndPage = communityService.getPostsAndPage(pageNumber, sort, filter, search);
        List<Category> categories = categoryService.getAllCategories(COMMUNITY.getCode());

        model.addAttribute("page", communitiesAndPage.getPage());
        model.addAttribute("posts", communitiesAndPage.getPosts());
        model.addAttribute("categories", categories);

        return VIEWS_COMMUNITY_LIST;
    }

    @LoginRequest
    @GetMapping("/community/{postId}")
    public String getCommunity(@LoginUser User user,
                               @PathVariable Long postId,
                               Model model)
    {
        CommunityWithAnswersAndCommentsResult result
                = communityService.getCommunityWithAnswersAndComments(user.getId(), postId);

        model.addAttribute("post", result.getCommunity());
        model.addAttribute("bookmarked", result.isBookmarked());
        model.addAttribute("answersWithComments", result.getAnswersWithComments());

        return VIEWS_COMMUNITY_DETAIL;
    }

    @LoginRequest
    @GetMapping("/community/createForm")
    public String getCreateForm(Model model)
    {
        List<Category> categories = categoryService.getAllCategories(COMMUNITY.getCode());
        model.addAttribute("categories", categories);
        return VIEWS_COMMUNITY_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/community/{communityId}/updateForm")
    public String getUpdateForm(@PathVariable Long communityId,
                                Model model)
    {

        List<Category> categories = categoryService.getAllCategories(COMMUNITY.getCode());
        CommunityResult community = communityService.getCommunity(communityId);

        model.addAttribute("categories", categories);
        model.addAttribute("post", community);

        return VIEWS_COMMUNITY_UPDATE_FORM;
    }
}

//첫 화면은 그냥 기존 ssr 방식대로 모든 html을 조립해서 준다.
// 이후 사용자가 정렬, 검색, 등등을 활용하면 json을 반환하도록 한다.
// 그럼 클라이언트 단에서 이 json을 가지고 html을 조립힌다.
