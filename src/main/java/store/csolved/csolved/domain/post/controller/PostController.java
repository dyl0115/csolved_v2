package store.csolved.csolved.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.answer.service.AnswerService;
import store.csolved.csolved.domain.answer.dto.AnswerWithComments;
import store.csolved.csolved.domain.category.dto.Category;
import store.csolved.csolved.domain.category.service.CategoryService;
import store.csolved.csolved.domain.post.dto.*;
import store.csolved.csolved.domain.post.dto.PostCountQuery;
import store.csolved.csolved.domain.post.dto.PostSearchQuery;
import store.csolved.csolved.domain.post.controller.dto.PostSearchRequest;
import store.csolved.csolved.domain.post.service.PopularPostService;
import store.csolved.csolved.domain.post.service.PostService;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

import static store.csolved.csolved.domain.post.dto.PostType.POST;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController
{
    public final static String VIEWS_POST_CREATE_FORM = "/views/post/create";
    public final static String VIEWS_POST_UPDATE_FORM = "/views/post/update";
    public final static String VIEWS_POST_LIST = "/views/post/list";
    public final static String VIEWS_POST_DETAIL = "/views/post/detail";
    public final static String VIEWS_POST_BEST = "/views/post/best";

    private final PostService postService;
    private final PopularPostService popularPostService;
    private final AnswerService answerService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/list")
    public String getPostCards(PostSearchRequest request, Model model)
    {
        Long total = postService.countPosts(PostCountQuery.from(request));
        Pagination pagination = Pagination.from(request.getPage(), total);
        PostSearchQuery query = PostSearchQuery.from(request, pagination);

        List<Category> categories = categoryService.getAllCategories(POST.getValue());
        List<PostCard> postCards = postService.getPostCards(query);
        List<PostSummary> bestPosts = popularPostService.getBestByPeriod(PeriodType.WEEK, 0L, 5L);
        List<PostSummary> mostViewedPosts = popularPostService.getMostViewed(PeriodType.WEEK, 7L);

        model.addAttribute("pagination", pagination);
        model.addAttribute("categories", categories);
        model.addAttribute("postCards", postCards);
        model.addAttribute("bestPosts", bestPosts);
        model.addAttribute("mostViewedPosts", mostViewedPosts);

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
        List<Category> categories = categoryService.getAllCategories(POST.getValue());

        model.addAttribute("categories", categories);

        return VIEWS_POST_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/{postId}/updateForm")
    public String getUpdateForm(@LoginUser User user,
                                @PathVariable Long postId,
                                Model model)
    {
        List<Category> categories = categoryService.getAllCategories(POST.getValue());
        Post post = postService.getPost(postId, user.getId());

        model.addAttribute("categories", categories);
        model.addAttribute("post", post);

        return VIEWS_POST_UPDATE_FORM;
    }

    @LoginRequest
    @GetMapping("/best")
    public String getBestPosts(@RequestParam PeriodType periodType,
                               @RequestParam Long page,
                               Model model)
    {

        Long total = popularPostService.countBestByPeriod(periodType);
        Pagination pagination = Pagination.from(page, total, 20L);
        List<PostSummary> bestPosts = popularPostService.getBestByPeriod(periodType, pagination);

        model.addAttribute("currentPeriod", periodType.name());
        model.addAttribute("postsCount", total);
        model.addAttribute("bestPosts", bestPosts);
        model.addAttribute("pagination", pagination);

        return VIEWS_POST_BEST;
    }
}
