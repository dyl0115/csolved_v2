package store.babel.babel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.answer.service.AnswerService;
import store.babel.babel.domain.answer.dto.AnswerWithComments;
import store.babel.babel.domain.category.dto.Category;
import store.babel.babel.domain.category.service.CategoryService;
import store.babel.babel.domain.post.dto.Post;
import store.babel.babel.domain.post.dto.PostCard;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.domain.post.dto.PostSearchQuery;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.filter.FilterInfo;
import store.babel.babel.global.utils.filter.Filtering;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.global.utils.page.PageInfo;
import store.babel.babel.global.utils.page.Pagination;
import store.babel.babel.global.utils.search.SearchInfo;
import store.babel.babel.global.utils.search.Searching;
import store.babel.babel.global.utils.sort.SortInfo;
import store.babel.babel.global.utils.sort.Sorting;

import java.util.List;

import static store.babel.babel.domain.post.dto.PostType.COMMUNITY;

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
    private final AnswerService answerService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/list")
    public String getPostCards(@PageInfo Long pageNumber,
                               @SortInfo Sorting sort,
                               @FilterInfo Filtering filter,
                               @SearchInfo Searching search,
                               Model model)
    {
        PostSearchQuery query = PostSearchQuery.from(pageNumber, sort, filter, search);

        Pagination pagination = Pagination.from(pageNumber, postService.countPosts(query));
        List<Category> categories = categoryService.getAllCategories(COMMUNITY.getCode());
        List<PostCard> postCards = postService.getPostCards(query, pagination);

        model.addAttribute("pagination", pagination);
        model.addAttribute("categories", categories);
        model.addAttribute("postCards", postCards);

        return VIEWS_POST_LIST;
    }

    @LoginRequest
    @GetMapping("/{postId}")
    public String getPost(@LoginUser User user,
                          @PathVariable Long postId,
                          @RequestParam(required = false) Boolean skipView,
                          Model model)
    {
        if (skipView == null) postService.increaseView(postId);

        Post post = postService.getPost(postId, user.getId());
        List<AnswerWithComments> answers = answerService.getAnswersWithComments(postId);

        model.addAttribute("post", post);
        model.addAttribute("answers", answers);

        return VIEWS_POST_DETAIL;
    }

    @LoginRequest
    @GetMapping("/{postId}/admin")
    public String getPostForAdmin(@LoginUser User user,
                                  @PathVariable Long postId,
                                  Model model)
    {
        Post post = postService.getPostForAdmin(postId, user.getId());
        List<AnswerWithComments> answers = answerService.getAnswersWithComments(postId);

        model.addAttribute("post", post);
        model.addAttribute("answers", answers);

        return VIEWS_POST_DETAIL;
    }

    @LoginRequest
    @GetMapping("/createForm")
    public String getCreateForm(Model model)
    {
        List<Category> categories
                = categoryService.getAllCategories(COMMUNITY.getCode());

        model.addAttribute("categories", categories);

        return VIEWS_POST_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/{postId}/updateForm")
    public String getUpdateForm(@LoginUser User user,
                                @PathVariable Long postId,
                                Model model)
    {
        List<Category> categories
                = categoryService.getAllCategories(COMMUNITY.getCode());
        Post post = postService.getPost(postId, user.getId());

        model.addAttribute("categories", categories);
        model.addAttribute("post", post);

        return VIEWS_POST_UPDATE_FORM;
    }
}
