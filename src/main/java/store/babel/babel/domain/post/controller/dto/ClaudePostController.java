package store.babel.babel.domain.post.controller.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.babel.babel.domain.category.dto.Category;
import store.babel.babel.domain.category.service.CategoryService;
import store.babel.babel.domain.post.controller.claude.ClaudeMessage;
import store.babel.babel.domain.post.dto.Post;
import store.babel.babel.domain.post.dto.PostType;
import store.babel.babel.domain.post.service.ClaudePostService;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ClaudePostController
{
    private static final String VIEW_AI_POST_CREATE_FORM = "/views/post/claude_create";

    private final ClaudePostService claudePostService;
    private final CategoryService categoryService;

    @LoginRequest
    @GetMapping("/ai/post/createForm")
    public String aiCreateForm(Model model)
    {
        List<Category> categories = categoryService.getAllCategories(PostType.POST.getValue());
        model.addAttribute("categories", categories);
        return VIEW_AI_POST_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/ai/post/connect")
    public SseEmitter connect(@LoginUser User user)
    {
        return claudePostService.connect(user.getId());
    }

    @LoginRequest
    @PostMapping("/ai/post/stream")
    public void stream(@LoginUser User user,
                       @RequestBody ClaudeMessage message)
    {
        claudePostService.stream(user.getId(), message);
    }
}
