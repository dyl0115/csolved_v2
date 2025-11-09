package store.csolved.csolved.domain.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.answer.controller.form.AnswerCreateForm;
import store.csolved.csolved.domain.category.Category;
import store.csolved.csolved.domain.category.service.CategoryService;
import store.csolved.csolved.domain.community.service.CommunityService;
import store.csolved.csolved.domain.community.service.result.CommunityAndPageResult;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.domain.comment.controller.form.CommentCreateForm;
import store.csolved.csolved.domain.community.controller.form.CommunityCreateUpdateForm;
import store.csolved.csolved.domain.community.controller.view_model.CommunityCreateUpdateVM;
import store.csolved.csolved.domain.community.controller.view_model.CommunityDetailVM;
import store.csolved.csolved.domain.community.controller.view_model.CommunityListVM;
import store.csolved.csolved.domain.community.service.CommunityFacade;
import store.csolved.csolved.utils.filter.FilterInfo;
import store.csolved.csolved.utils.filter.Filtering;
import store.csolved.csolved.utils.login.LoginUser;
import store.csolved.csolved.utils.page.PageInfo;
import store.csolved.csolved.utils.search.SearchInfo;
import store.csolved.csolved.utils.search.Searching;
import store.csolved.csolved.utils.sort.SortInfo;
import store.csolved.csolved.utils.sort.Sorting;

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

    private final CommunityFacade communityFacade;

    private final CommunityService communityService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/communities")
    public String getCommunityPosts(@PageInfo Long pageNumber,
                                    @SortInfo Sorting sort,
                                    @FilterInfo Filtering filter,
                                    @SearchInfo Searching search,
                                    Model model)
    {
        CommunityAndPageResult communitiesAndPage = communityService.getCommunitiesAndPage(pageNumber, sort, filter, search);
        List<Category> categories = categoryService.getAll(COMMUNITY.getCode());

        model.addAttribute("page", communitiesAndPage.getPage());
        model.addAttribute("posts", communitiesAndPage.getCommunities());
        model.addAttribute("categories", categories);

        return VIEWS_COMMUNITY_LIST;
    }

    @LoginRequest
    @GetMapping("/community/{postId}")
    public String viewPost(@LoginUser User user,
                           @PathVariable Long postId,
                           Model model)
    {
        CommunityDetailVM communityPost = communityFacade.viewPost(user.getId(), postId);
        model.addAttribute("communityPostDetails", communityPost);
        model.addAttribute("answerCreateForm", AnswerCreateForm.empty());
        model.addAttribute("commentCreateForm", CommentCreateForm.empty());
        return VIEWS_COMMUNITY_DETAIL;
    }

    @LoginRequest
    @GetMapping("/community/{postId}/read")
    public String getPost(@LoginUser User user,
                          @PathVariable Long postId,
                          Model model)
    {
        CommunityDetailVM post = communityFacade.getPost(user.getId(), postId);
        model.addAttribute("communityPostDetails", post);
        model.addAttribute("answerCreateForm", AnswerCreateForm.empty());
        model.addAttribute("commentCreateForm", CommentCreateForm.empty());
        return VIEWS_COMMUNITY_DETAIL;
    }

    @LoginRequest
    @GetMapping("/community/createForm")
    public String initCreate(Model model)
    {
        CommunityCreateUpdateVM viewModel = communityFacade.initCreate();
        model.addAttribute("createVM", viewModel);
        model.addAttribute("createForm", CommunityCreateUpdateForm.empty());
        return VIEWS_COMMUNITY_CREATE_FORM;
    }

    @LoginRequest
    @PostMapping("/community")
    public String processCreate(@Valid @ModelAttribute("createForm") CommunityCreateUpdateForm form,
                                BindingResult result,
                                Model model)
    {
        if (result.hasErrors())
        {
            CommunityCreateUpdateVM viewModel = communityFacade.initCreate();
            model.addAttribute("createVM", viewModel);
            return VIEWS_COMMUNITY_CREATE_FORM;
        }
        else
        {
            communityFacade.save(form);
            return "redirect:/communities?page=1";
        }
    }

    @LoginRequest
    @GetMapping("/community/{postId}/updateForm")
    public String initUpdate(@PathVariable Long postId,
                             Model model)
    {
        CommunityCreateUpdateVM viewModel = communityFacade.initUpdate();
        model.addAttribute("updateVM", viewModel);
        CommunityCreateUpdateForm form = communityFacade.initUpdateForm(postId);
        model.addAttribute("updateForm", form);
        return VIEWS_COMMUNITY_UPDATE_FORM;
    }

    @LoginRequest
    @PutMapping("/community/{postId}")
    public String processUpdate(@PathVariable("postId") Long postId,
                                @Valid @ModelAttribute("updateForm") CommunityCreateUpdateForm form,
                                BindingResult result,
                                Model model)
    {
        if (result.hasErrors())
        {
            CommunityCreateUpdateVM viewModel = communityFacade.initUpdate();
            model.addAttribute("updateVM", viewModel);
            return VIEWS_COMMUNITY_UPDATE_FORM;
        }

        communityFacade.update(postId, form);
        return "redirect:/communities?page=1";
    }
}

//첫 화면은 그냥 기존 ssr 방식대로 모든 html을 조립해서 준다.
// 이후 사용자가 정렬, 검색, 등등을 활용하면 json을 반환하도록 한다.
// 그럼 클라이언트 단에서 이 json을 가지고 html을 조립힌다.
