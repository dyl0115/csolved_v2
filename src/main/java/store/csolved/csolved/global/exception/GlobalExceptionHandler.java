package store.csolved.csolved.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(CsolvedException.class)
    public ResponseEntity<ExceptionResponse> handleCsovledException(CsolvedException exception)
    {
        ExceptionResponse exceptionResponse
                = ExceptionResponse.from(exception.getCode());

        return ResponseEntity.status(exceptionResponse.status)
                .body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception)
    {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error ->
                {
                    errors.put(error.getField(), error.getDefaultMessage());
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", errors));
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
