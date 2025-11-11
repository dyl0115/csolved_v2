package store.csolved.csolved.domain.file.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageSaveResponse
{
    private String imageUrl;

    public static ImageSaveResponse from(String imageUrl)
    {
        return ImageSaveResponse.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
