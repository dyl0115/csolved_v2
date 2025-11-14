package store.postHub.postHub.global.exception;

import lombok.Data;

@Data
public class CsolvedException extends RuntimeException
{
    private ExceptionCode code;

    public CsolvedException(ExceptionCode exceptionCode)
    {
        this.code = exceptionCode;
    }
}
