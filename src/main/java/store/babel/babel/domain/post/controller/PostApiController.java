package store.babel.babel.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.post.controller.dto.PostCreateRequest;
import store.babel.babel.domain.post.controller.dto.PostUpdateRequest;
import store.babel.babel.domain.post.service.PostService;
import store.babel.babel.domain.post.dto.PostCreateCommand;
import store.babel.babel.domain.post.dto.PostUpdateCommand;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.domain.user.User;

@RequestMapping("/api/post")
@RequiredArgsConstructor
@RestController
public class PostApiController
{
    private final PostService postService;

    @LoginRequest
    @PostMapping
    public void createPost(@Valid @RequestBody PostCreateRequest request)
    {
        postService.createPost(PostCreateCommand.from(request));
    }

    @LoginRequest
    @PutMapping
    public void updatePost(@Valid @RequestBody PostUpdateRequest request)
    {
        postService.updatePost(PostUpdateCommand.from(request));
    }

    @LoginRequest
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId)
    {
        postService.deletePost(postId);
    }

    @LoginRequest
    @PostMapping("/{postId}/likes")
    public void addLike(@LoginUser User user,
                        @PathVariable Long postId)
    {
        postService.addLike(postId, user.getId());
    }
}