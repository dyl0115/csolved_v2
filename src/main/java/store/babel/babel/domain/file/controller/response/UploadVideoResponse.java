package store.babel.babel.domain.file.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadVideoResponse
{
    private String videoUrl;

    public static UploadVideoResponse from(String videoUrl)
    {
        return UploadVideoResponse.builder()
                .videoUrl(videoUrl)
                .build();
    }
}
