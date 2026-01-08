package store.csolved.csolved.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.post.controller.dto.PostCreateRequest;
import store.csolved.csolved.domain.post.controller.dto.PostUpdateRequest;
import store.csolved.csolved.domain.post.service.PostService;
import store.csolved.csolved.domain.post.dto.PostCreateCommand;
import store.csolved.csolved.domain.post.dto.PostUpdateCommand;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.global.utils.login.LoginUser;
import store.csolved.csolved.domain.user.dto.User;

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
    public void deletePost(@LoginUser User user,
                           @PathVariable Long postId)
    {
        postService.deletePost(postId, user.getId());
    }

    @LoginRequest
    @PostMapping("/{postId}/likes")
    public void addLike(@LoginUser User user,
                        @PathVariable Long postId)
    {
        postService.addLike(postId, user.getId());
    }
}