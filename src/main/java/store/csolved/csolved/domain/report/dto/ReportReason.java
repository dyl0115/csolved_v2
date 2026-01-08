package store.csolved.csolved.domain.report.dto;

import lombok.Getter;

@Getter
public enum ReportReason
{
    SPAM("스팸/광고"),
    ABUSIVE_LANGUAGE("욕설/비방"),
    SEXUAL_CONTENT("음란물/선정성"),
    VIOLENCE("폭력적 콘텐츠"),
    HATE_SPEECH("혐오 발언"),
    FALSE_INFO("허위 정보"),
    COPYRIGHT("저작권 침해"),
    PRIVACY("개인정보 노출"),
    ILLEGAL("불법 정보"),
    ETC("기타");

    private final String description;

    ReportReason(String description)
    {
        this.description = description;
    }
}
