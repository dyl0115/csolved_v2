package store.babel.babel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.category.CategoryResult;
import store.babel.babel.domain.category.service.CategoryService;
import store.babel.babel.domain.post.dto.Post;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.domain.post.dto.PostSearchQuery;
import store.babel.babel.domain.post.dto.PostCardWithPage;
import store.babel.babel.domain.post.dto.PostWithAnswers;
import store.babel.babel.domain.user.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.filter.FilterInfo;
import store.babel.babel.global.utils.filter.Filtering;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.global.utils.page.PageInfo;
import store.babel.babel.global.utils.search.SearchInfo;
import store.babel.babel.global.utils.search.Searching;
import store.babel.babel.global.utils.sort.SortInfo;
import store.babel.babel.global.utils.sort.Sorting;

import java.util.List;

import static store.babel.babel.common.PostType.COMMUNITY;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController
{
    public final static String VIEWS_POST_CREATE_FORM = "/views/post/create";
    public final static String VIEWS_POST_UPDATE_FORM = "/views/post/update";
    public final static String VIEWS_POST_LIST = "/views/post/list";
    public final static String VIEWS_POST_DETAIL = "/views/post/detail";

    private final PostService postService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/list")
    public String getPosts(@PageInfo Long pageNumber,
                           @SortInfo Sorting sort,
                           @FilterInfo Filtering filter,
                           @SearchInfo Searching search,
                           Model model)
    {
        //TODO: PostDetailResult 말고, PostSummaryResult 를 생성할 것
        PostCardWithPage postsAndPagination
                = postService.getPostCardsAndPage(PostSearchQuery.from(pageNumber, sort, filter, search));
        List<CategoryResult> categories
                = categoryService.getAllCategories(COMMUNITY.getCode());

        model.addAttribute("posts", postsAndPagination.getPostCards());
        model.addAttribute("pagination", postsAndPagination.getPagination());
        model.addAttribute("categories", categories);

        return VIEWS_POST_LIST;
    }

    @LoginRequest
    @GetMapping("/{postId}")
    public String getPost(@LoginUser User user,
                          @PathVariable Long postId,
                          Model model)
    {
        PostWithAnswers post
                = postService.getPostDetailWithAnswersAndComments(user.getId(), postId);
        model.addAttribute("post", post.getPost());
        return VIEWS_POST_DETAIL;
    }

    @LoginRequest
    @GetMapping("/createForm")
    public String getCreateForm(Model model)
    {
        List<CategoryResult> categories
                = categoryService.getAllCategories(COMMUNITY.getCode());
        model.addAttribute("categories", categories);
        return VIEWS_POST_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/{communityId}/updateForm")
    public String getUpdateForm(@PathVariable Long communityId,
                                Model model)
    {
        List<CategoryResult> categories
                = categoryService.getAllCategories(COMMUNITY.getCode());
        Post post = postService.getPostDetail(communityId);

        model.addAttribute("categories", categories);
        model.addAttribute("post", post);

        return VIEWS_POST_UPDATE_FORM;
    }
}
