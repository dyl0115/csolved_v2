package store.csolved.csolved.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.auth.controller.response.SignInResponse;
import store.csolved.csolved.domain.auth.controller.response.SignOutResponse;
import store.csolved.csolved.domain.auth.controller.response.SignUpResponse;
import store.csolved.csolved.domain.auth.service.command.SignInCommand;
import store.csolved.csolved.domain.auth.service.command.SignUpCommand;
import store.csolved.csolved.utils.login.LoginRequest;
import store.csolved.csolved.domain.auth.service.AuthService;
import store.csolved.csolved.utils.login.LoginUser;
import store.csolved.csolved.domain.user.User;
import store.csolved.csolved.domain.auth.controller.request.SignInRequest;
import store.csolved.csolved.domain.auth.controller.request.SignUpRequest;

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