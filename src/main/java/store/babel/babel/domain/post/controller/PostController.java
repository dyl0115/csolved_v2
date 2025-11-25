package store.babel.babel.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.answer.service.AnswerService;
import store.babel.babel.domain.answer.dto.AnswerWithComments;
import store.babel.babel.domain.category.dto.Category;
import store.babel.babel.domain.category.service.CategoryService;
import store.babel.babel.domain.post.dto.*;
import store.babel.babel.domain.post.dto.PostCountQuery;
import store.babel.babel.domain.post.dto.PostSearchQuery;
import store.babel.babel.domain.post.controller.dto.PostSearchRequest;
import store.babel.babel.domain.post.service.PopularPostService;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

import static store.babel.babel.domain.post.dto.PostType.POST;

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
    public String getBestPosts(@RequestParam(defaultValue = "WEEK") String period,
                               @RequestParam(required = false) Long category,
                               Model model)
    {
        PeriodType periodType = PeriodType.valueOf(period);

        List<PostSummary> bestPosts = popularPostService.getBestByPeriod(periodType, 0L, 20L);
        List<Category> categories = categoryService.getAllCategories(POST.getValue());

        // 카테고리 필터링
        if (category != null)
        {
            bestPosts = bestPosts.stream()
                    .filter(post -> post.getCategoryId() != null && post.getCategoryId().equals(category))
                    .toList();
        }

        model.addAttribute("currentPeriod", period);
        model.addAttribute("bestPosts", bestPosts);
        model.addAttribute("categories", categories);

        return VIEWS_POST_BEST;
    }
}
