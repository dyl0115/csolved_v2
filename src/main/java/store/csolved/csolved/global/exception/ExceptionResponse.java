package store.csolved.csolved.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ExceptionResponse
{
    String code;
    String message;
    HttpStatus status;

    public static ExceptionResponse from(ExceptionCode exceptionCode)
    {
        return new ExceptionResponse(exceptionCode.getCode(), exceptionCode.getMessage(), exceptionCode.getStatus());
    }
}
