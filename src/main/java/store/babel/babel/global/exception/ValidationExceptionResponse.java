package store.babel.babel.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class ValidationExceptionResponse
{
    private String code;
    private String message;
    private Map<String, String> errors;

    public static ValidationExceptionResponse from(MethodArgumentNotValidException exception)
    {
        Map<String, String> errors = exception
                .getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error ->
                        {
                            String message = error.getDefaultMessage();
                            return message != null ? message : "유효하지 않은 값입니다.";
                        },
                        (existing, replacement) -> existing));

        return ValidationExceptionResponse.builder()
                .code("VALIDATION_EXCEPTION")
                .message("입력값이 올바르지 않습니다.")
                .errors(errors)
                .build();
    }
}
