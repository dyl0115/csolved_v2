package store.babel.babel.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import store.babel.babel.domain.user.User;
import store.babel.babel.domain.user.controller.form.UserProfileForm;
import store.babel.babel.domain.user.mapper.UserMapper;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class UpdateProfileValidator implements Validator
{
    private final UserMapper userMapper;

    @Override
    public boolean supports(Class<?> clazz)
    {
        return UserProfileForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        UserProfileForm form = (UserProfileForm) target;
        checkNicknameDuplicate(form, errors);
    }

    // 닉네임 중복 체크
    private void checkNicknameDuplicate(UserProfileForm form, Errors errors)
    {
        User user = userMapper.findUserById(form.getUserId());

        // 자신의 닉네임이 그대로 입력된 경우
        if (Objects.equals(user.getNickname(), form.getNickname()))
        {
            return;
        }

        // 이미 존재하는 닉네임인 경우
        if (userMapper.existsByNickname(form.getNickname()))
        {
            errors.rejectValue("nickname", "duplicate", "이미 사용중인 닉네임입니다.");
        }
    }
}