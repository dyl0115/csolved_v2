package store.csolved.csolved.domain.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.community.controller.request.CommunityCreateRequest;
import store.csolved.csolved.domain.community.controller.request.CommunityUpdateRequest;
import store.csolved.csolved.domain.community.service.CommunityService;
import store.csolved.csolved.domain.community.service.command.CommunityCreateCommand;
import store.csolved.csolved.domain.community.service.command.CommunityUpdateCommand;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.utils.login.LoginUser;
import store.csolved.csolved.domain.community.service.CommunityFacade;
import store.csolved.csolved.domain.user.User;

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
