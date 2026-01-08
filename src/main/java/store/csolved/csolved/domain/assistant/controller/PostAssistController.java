package store.csolved.csolved.domain.assistant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.category.dto.Category;
import store.csolved.csolved.domain.category.service.CategoryService;
import store.csolved.csolved.domain.post.dto.Post;
import store.csolved.csolved.domain.post.dto.PostType;
import store.csolved.csolved.domain.post.service.PostService;
import store.csolved.csolved.domain.user.dto.User;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;

import java.util.List;

import static store.csolved.csolved.domain.post.dto.PostType.POST;

@RequiredArgsConstructor
@RequestMapping("/ai/post/")
@Controller
public class PostAssistController
{
    private static final String VIEW_AI_POST_CREATE_FORM = "/views/post/assistant_create";
    private static final String VIEW_AI_POST_UPDATE_FORM = "/views/post/assistant_update";

    private final CategoryService categoryService;
    private final PostService postService;

    @LoginRequest
    @GetMapping("/createForm")
    public String aiCreateForm(Model model)
    {
        List<Category> categories = categoryService.getAllCategories(PostType.POST.getValue());
        model.addAttribute("categories", categories);
        return VIEW_AI_POST_CREATE_FORM;
    }

    @LoginRequest
    @GetMapping("/{postId}/updateForm")
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
}
