package store.babel.babel.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.babel.babel.domain.auth.controller.dto.SignInResponse;
import store.babel.babel.domain.auth.controller.dto.SignOutResponse;
import store.babel.babel.domain.auth.controller.dto.SignUpResponse;
import store.babel.babel.domain.auth.service.command.SignInCommand;
import store.babel.babel.domain.auth.service.command.SignUpCommand;
import store.babel.babel.global.utils.login.LoginRequest;
import store.babel.babel.domain.auth.service.AuthService;
import store.babel.babel.global.utils.login.LoginUser;
import store.babel.babel.domain.user.dto.User;
import store.babel.babel.domain.auth.controller.dto.SignInRequest;
import store.babel.babel.domain.auth.controller.dto.SignUpRequest;

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
    public void withdraw(@LoginUser User user)
    {
        authService.withdraw(user);
    }
}