package store.postHub.postHub.domain.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import store.postHub.postHub.domain.file.controller.response.UploadImageResponse;
import store.postHub.postHub.domain.file.controller.response.UploadVideoResponse;
import store.postHub.postHub.domain.file.service.FileService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileController
{
    private final FileService fileService;

    @PostMapping("/api/image")
    public UploadImageResponse imageSave(@RequestParam("image") MultipartFile imageFile) throws IOException
    {
        String imageUrl = fileService.uploadImage(imageFile);
        return UploadImageResponse.from(imageUrl);
    }

    @PostMapping("/api/video")
    public UploadVideoResponse videoSave(@RequestParam("video") MultipartFile videoFile) throws IOException
    {
        String videoUrl = fileService.uploadVideo(videoFile);
        return UploadVideoResponse.from(videoUrl);
    }
}
