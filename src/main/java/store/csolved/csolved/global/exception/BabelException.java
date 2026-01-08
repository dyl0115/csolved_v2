package store.csolved.csolved.global.exception;

import lombok.Data;

@Data
public class BabelException extends RuntimeException
{
    private ExceptionCode code;

    public BabelException(ExceptionCode exceptionCode)
    {
        this.code = exceptionCode;
    }
}
