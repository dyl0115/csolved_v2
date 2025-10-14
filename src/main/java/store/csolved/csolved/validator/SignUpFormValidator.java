package store.csolved.csolved.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import store.csolved.csolved.domain.auth.controller.request.SignUpRequest;
import store.csolved.csolved.domain.user.mapper.UserMapper;

@RequiredArgsConstructor
@Component
public class SignUpFormValidator implements Validator
{
    private final UserMapper userMapper;

    @Override
    public boolean supports(Class<?> clazz)
    {
        return SignUpRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        SignUpRequest form = (SignUpRequest) target;

        checkEmailDuplicate(form, errors);
        checkNicknameDuplicate(form, errors);
        checkPasswordMatch(form, errors);
    }

    private void checkEmailDuplicate(SignUpRequest form, Errors errors)
    {
        if (userMapper.existsByEmail(form.getEmail()))
        {
            errors.rejectValue("email", "duplicate", "이미 사용중인 이메일입니다.");
        }
    }

    private void checkNicknameDuplicate(SignUpRequest form, Errors errors)
    {
        if (userMapper.existsByNickname(form.getNickname()))
        {
            errors.rejectValue("nickname", "duplicate", "이미 사용중인 닉네임입니다.");
        }
    }

    private void checkPasswordMatch(SignUpRequest form, Errors errors)
    {
        if (!form.getPassword().equals(form.getPasswordConfirm()))
        {
            errors.rejectValue("passwordConfirm", "notMatch", "비밀번호가 일치하지 않습니다.");
        }
    }
}
