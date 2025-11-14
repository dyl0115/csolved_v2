package store.babel.babel.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.IOException;

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

    public static ExceptionResponse from(IOException exception)
    {
        return new ExceptionResponse(ExceptionCode.INTERNAL_SERVER_ERROR.getCode(), exception.getMessage(), ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
