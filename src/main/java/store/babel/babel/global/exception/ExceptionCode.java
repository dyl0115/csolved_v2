package store.babel.babel.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode
{
    // 게시글
    POST_NOT_FOUND("P001", "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    POST_UPDATE_DENIED("P002", "글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    POST_DELETE_DENIED("P003", "글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_LIKED("P004", "좋아요는 한번만 누를 수 있습니다.", HttpStatus.CONFLICT),
    ALREADY_BOOKMARKED("P005", "이미 북마크한 게시글입니다.", HttpStatus.CONFLICT),

    // 답변
    ANSWER_NOT_FOUND("C001", "존재하지 않는 답변입니다.", HttpStatus.NOT_FOUND),
    ANSWER_SAVE_DENIED("C002", "답변을 작성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ANSWER_DELETE_DENIED("C003", "답변을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 공지사항
    NOTICE_NOT_FOUND("N001", "존재하지 않는 공지사항입니다.", HttpStatus.NOT_FOUND),
    NOTICE_SAVE_DENIED("N002", "글을 작성할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOTICE_UPDATE_DENIED("N003", "글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOTICE_DELETE_DENIED("N004", "글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOTICE_ADMIN_ONLY("N005", "관리자 권한이 필요합니다.", HttpStatus.FORBIDDEN),

    // 인증
    DUPLICATE_EMAIL("A001", "이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME("A002", "이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("A003", "비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH("A004", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("A005", "등록되지 않은 사용자입니다.", HttpStatus.NOT_FOUND),
    INVALID_SESSION("A006", "인증되지 않은 세션입니다.", HttpStatus.UNAUTHORIZED),

    // 채팅
    CHAT_SESSION_NOT_FOUND("H001", "채팅 세션이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    AI_RESOURCE_EXHAUSTED("H002", "AI 사용 가능 횟수를 초과했습니다. 토큰을 충전 후 이용해주세요.", HttpStatus.TOO_MANY_REQUESTS),

    // 사용자 프로필
    PROFILE_UPDATE_DENIED("U001", "프로필을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 기타
    IMAGE_UPLOAD_FAILED("G001", "이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    ACCESS_DENIED("G002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    INTERNAL_SERVER_ERROR("G003", "서버에 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
