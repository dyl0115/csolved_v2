package store.csolved.csolved.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController
{
    public final static String VIEWS_SIGN_IN_FORM = "/views/auth/signIn";
    public final static String VIEWS_SIGN_UP_FORM = "/views/auth/signUp";

    @GetMapping("/signIn")
    public String signInView()
    {
        return VIEWS_SIGN_IN_FORM;
    }

    @GetMapping("/signUp")
    public String initSignUp()
    {
        return VIEWS_SIGN_UP_FORM;
    }
}
