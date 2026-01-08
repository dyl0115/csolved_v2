package store.csolved.csolved.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.csolved.csolved.domain.auth.controller.dto.*;
import store.csolved.csolved.domain.auth.service.command.SignInCommand;
import store.csolved.csolved.domain.auth.service.command.SignUpCommand;
import store.csolved.csolved.global.utils.login.LoginRequest;
import store.csolved.csolved.domain.auth.service.AuthService;
import store.csolved.csolved.global.utils.login.LoginUser;
import store.csolved.csolved.domain.user.dto.User;

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
    @PutMapping("/password")
    public void updatePassword(@LoginUser User user,
                               @Valid @RequestBody PasswordUpdateRequest request)
    {
        authService.updatePassword(user.getId(), PasswordUpdateCommand.from(request));
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
    public void withdraw(@LoginUser User user,
                         @Valid @RequestBody WithdrawRequest request)
    {
        authService.withdraw(user, WithdrawCommand.from(request));
    }


}