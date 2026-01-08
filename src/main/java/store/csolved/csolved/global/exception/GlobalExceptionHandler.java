package store.csolved.csolved.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(BabelException.class)
    public ResponseEntity<ExceptionResponse> handleBabelException(BabelException exception)
    {
        ExceptionResponse exceptionResponse
                = ExceptionResponse.from(exception.getCode());

        return ResponseEntity.status(exceptionResponse.status)
                .body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleValidationException(MethodArgumentNotValidException exception)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ValidationExceptionResponse.from(exception));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> handleIOException(IOException exception)
    {
        ExceptionResponse exceptionResponse
                = ExceptionResponse.from(exception);
        return ResponseEntity.status(exceptionResponse.status)
                .body(exceptionResponse);
    }
}
