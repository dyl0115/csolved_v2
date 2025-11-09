package store.csolved.csolved.domain.community.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommunityUpdateResponse
{
    private Boolean success;
    private String message;

    public static CommunityUpdateResponse success()
    {
        return CommunityUpdateResponse.builder()
                .success(true)
                .message("수정이 완료되었습니다.")
                .build();
    }
}
