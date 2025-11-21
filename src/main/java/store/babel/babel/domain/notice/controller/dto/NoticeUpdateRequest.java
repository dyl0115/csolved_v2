package store.babel.babel.domain.notice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import store.babel.babel.domain.notice.dto.Notice;

@Getter
@Builder
public class NoticeUpdateRequest
{
    @NotNull
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 2, max = 50, message = "제목은 최소 2글자에서 50자까지 가능합니다.")
    private String title;

    @NotNull
    private Long authorId;

    @NotNull(message = "실명/익명 여부를 선택해주세요.")
    private boolean anonymous;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    public static NoticeUpdateRequest from(Notice notice)
    {
        return NoticeUpdateRequest.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .authorId(notice.getAuthorId())
                .anonymous(notice.isAnonymous())
                .build();
    }
}
