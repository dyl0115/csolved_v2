package store.csolved.csolved.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode
{
    // 게시글
    POST_NOT_FOUND("POST_NOT_FOUND", "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    POST_UPDATE_DENIED("POST_UPDATE_DENIED", "글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    POST_DELETE_DENIED("POST_DELETE_DENIED", "글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_LIKED("ALREADY_LIKED", "좋아요는 한번만 누를 수 있습니다.", HttpStatus.CONFLICT),

    // 답변
    ANSWER_NOT_FOUND("ANSWER_NOT_FOUND", "존재하지 않는 답변입니다.", HttpStatus.NOT_FOUND),
    ANSWER_SAVE_DENIED("ANSWER_SAVE_DENIED", "답변을 작성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ANSWER_DELETE_DENIED("ANSWER_DELETE_DENIED", "답변을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 공지사항
    NOTICE_NOT_FOUND("NOTICE_NOT_FOUND", "존재하지 않는 공지사항입니다.", HttpStatus.NOT_FOUND),
    NOTICE_SAVE_DENIED("NOTICE_SAVE_DENIED", "글을 작성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOTICE_UPDATE_DENIED("NOTICE_UPDATE_DENIED", "글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOTICE_DELETE_DENIED("NOTICE_DELETE_DENIED", "글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOTICE_ADMIN_ONLY("NOTICE_ADMIN_ONLY", "관리자 권한이 필요합니다.", HttpStatus.FORBIDDEN),

    // 인증
    DUPLICATE_EMAIL("A001", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME("A002", "이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("A003", "비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH("A004", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("A005", "등록되지 않은 사용자입니다.", HttpStatus.NOT_FOUND),
    INVALID_SESSION("A006", "인증되지 않은 세션입니다.", HttpStatus.UNAUTHORIZED),


    // 북마크
    ALREADY_BOOKMARKED("ALREADY_BOOKMARKED", "이미 북마크한 게시글입니다.", HttpStatus.CONFLICT),

    // 기타
    IMAGE_UPLOAD_FAILED("IMAGE_UPLOAD_FAILED", "이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED("ACCESS_DENIED", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR("G001", "서버에 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
