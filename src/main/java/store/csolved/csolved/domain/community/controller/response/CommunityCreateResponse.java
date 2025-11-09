package store.csolved.csolved.domain.community.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommunityCreateResponse
{
    private Boolean success;
    private String message;

    public static CommunityCreateResponse success()
    {
        return CommunityCreateResponse.builder()
                .success(true)
                .message("저장이 완료되었습니다.")
                .build();
    }
}
