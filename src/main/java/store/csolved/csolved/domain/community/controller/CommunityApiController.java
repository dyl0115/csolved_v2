package store.csolved.csolved.domain.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.community.controller.request.CommunityUpdateRequest;
import store.csolved.csolved.domain.community.controller.response.CommunityUpdateResponse;
import store.csolved.csolved.domain.community.controller.view_model.CommunityCreateUpdateVM;
import store.csolved.csolved.domain.community.service.CommunityService;
import store.csolved.csolved.domain.community.service.command.CommunityUpdateCommand;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.utils.login.LoginUser;
import store.csolved.csolved.domain.community.service.CommunityFacade;
import store.csolved.csolved.domain.user.User;

import java.util.Map;

@RequestMapping("/api/community")
@RequiredArgsConstructor
@RestController
public class CommunityApiController
{
    private final CommunityFacade communityFacade;

    private final CommunityService communityService;

    @LoginRequest
    @PostMapping
    public String createPost(@Valid @ModelAttribute("createForm") CommunityUpdateRequest form,
                             BindingResult result,
                             Model model)
    {
//        if (result.hasErrors())
//        {
//            CommunityCreateUpdateVM viewModel = communityFacade.initCreate();
//            model.addAttribute("createVM", viewModel);
//            return VIEWS_COMMUNITY_CREATE_FORM;
//        }
//        else
//        {
//            communityFacade.save(form);
//            return "redirect:/communities?page=1";
//        }
        return null;
    }

    @LoginRequest
    @PutMapping("/{postId}")
    public CommunityUpdateResponse updatePost(@PathVariable("postId") Long postId,
                                              @Valid @RequestBody CommunityUpdateRequest request)
    {
        communityService.update(postId, CommunityUpdateCommand.from(request));
        return CommunityUpdateResponse.success();
    }

    @LoginRequest
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> addLike(@LoginUser User user,
                                        @PathVariable Long postId)
    {
        boolean valid = communityFacade.addLike(postId, user.getId());
        if (!valid)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @LoginRequest
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long postId)
    {
        communityFacade.delete(postId);
    }

}
