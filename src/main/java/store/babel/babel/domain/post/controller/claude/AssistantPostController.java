package store.babel.babel.domain.post.controller.claude;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.category.dto.Category;
import store.babel.babel.domain.category.service.CategoryService;
import store.babel.babel.domain.post.dto.Post;
import store.babel.babel.domain.post.dto.PostType;
import store.babel.babel.domain.post.service.ClaudePostService;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

import java.io.IOException;
import java.util.List;

import static store.babel.babel.domain.post.dto.PostType.POST;

@RequiredArgsConstructor
@Controller
public class AssistantPostController
{
    private static final String VIEW_AI_POST_CREATE_FORM = "/views/post/assistant_create";
    private static final String VIEW_AI_POST_UPDATE_FORM = "/views/post/assistant_update";

    private final ClaudePostService claudePostService;
    private final CategoryService categoryService;
    private final PostService postService;

    @LoginRequest
    @GetMapping("/ai/post/createForm")
    public String aiCreateForm(Model model)
    {
        List<Category> categories = categoryService.getAllCategories(PostType.POST.getValue());
        model.addAttribute("categories", categories);
        return VIEW_AI_POST_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/ai/post/{postId}/updateForm")
    public String aiUpdateForm(@LoginUser User user,
                               @PathVariable("postId") Long postId,
                               Model model)
    {
        List<Category> categories = categoryService.getAllCategories(POST.getValue());
        Post post = postService.getPost(postId, user.getId());

        model.addAttribute("categories", categories);
        model.addAttribute("post", post);
        model.addAttribute("user", user);

        return VIEW_AI_POST_UPDATE_FORM;
    }

    @LoginRequest
    @GetMapping("/ai/post/connect")
    public SseEmitter connect(@LoginUser User user)
    {
        return claudePostService.connect(user.getId());
    }

    @LoginRequest
    @ResponseBody
    @PostMapping("/ai/post/message")
    public void processMessage(@LoginUser User user,
                               @RequestBody ClaudeMessage message) throws IOException
    {
        claudePostService.stream(user.getId(), message);
    }
}
