package store.postHub.postHub.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.postHub.postHub.domain.auth.controller.response.SignInResponse;
import store.postHub.postHub.domain.auth.controller.response.SignOutResponse;
import store.postHub.postHub.domain.auth.controller.response.SignUpResponse;
import store.postHub.postHub.domain.auth.service.command.SignInCommand;
import store.postHub.postHub.domain.auth.service.command.SignUpCommand;
import store.postHub.postHub.global.utils.login.LoginRequest;
import store.postHub.postHub.domain.auth.service.AuthService;
import store.postHub.postHub.global.utils.login.LoginUser;
import store.postHub.postHub.domain.user.User;
import store.postHub.postHub.domain.auth.controller.request.SignInRequest;
import store.postHub.postHub.domain.auth.controller.request.SignUpRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthApiController
{
    private final AuthService authService;

    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    public SignInResponse signIn(@Valid @RequestBody SignInRequest request)
    {
        authService.signIn(SignInCommand.from(request));
        return SignInResponse.success();
    }

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request)
    {
        authService.signUp(SignUpCommand.from(request));
        return SignUpResponse.success();
    }

    @LoginRequest
    @PostMapping("/signOut")
    @ResponseStatus(HttpStatus.OK)
    public SignOutResponse signOut()
    {
        authService.signOut();
        return SignOutResponse.success();
    }

    @LoginRequest
    @DeleteMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void processWithdraw(@LoginUser User user)
    {
        authService.withdraw(user);
    }
}