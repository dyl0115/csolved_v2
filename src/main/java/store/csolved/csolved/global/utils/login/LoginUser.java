package store.csolved.csolved.global.utils.login;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface LoginUser
{
}
