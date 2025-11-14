package store.postHub.postHub.domain.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.community.controller.request.CommunityCreateRequest;
import store.postHub.postHub.domain.community.controller.request.CommunityUpdateRequest;
import store.postHub.postHub.domain.community.service.CommunityService;
import store.postHub.postHub.domain.community.service.command.CommunityCreateCommand;
import store.postHub.postHub.domain.community.service.command.CommunityUpdateCommand;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.global.utils.login.LoginUser;
import store.postHub.postHub.domain.user.User;

@RequestMapping("/api/community")
@RequiredArgsConstructor
@RestController
public class CommunityApiController
{
    private final CommunityService communityService;

    @LoginRequest
    @PostMapping
    public void createPost(@Valid @RequestBody CommunityCreateRequest request)
    {
        communityService.create(CommunityCreateCommand.from(request));
    }

    @LoginRequest
    @PutMapping("/{postId}")
    public void updatePost(@PathVariable("postId") Long postId,
                           @Valid @RequestBody CommunityUpdateRequest request)
    {
        communityService.update(postId, CommunityUpdateCommand.from(request));
    }

    @LoginRequest
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId)
    {
        communityService.delete(postId);
    }

    @LoginRequest
    @PostMapping("/{postId}/likes")
    public void addLike(@LoginUser User user,
                        @PathVariable Long postId)
    {
        communityService.addLike(postId, user.getId());
    }
}
