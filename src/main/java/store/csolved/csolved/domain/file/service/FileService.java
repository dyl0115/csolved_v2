package store.csolved.csolved.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService
{
    private final AmazonS3Client amazonS3Client;
    private final String folderName = "post/free-board";

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    public String uploadImage(MultipartFile imageFile) throws IOException
    {
        String fileName = createRandomFileName(imageFile);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());

        amazonS3Client.putObject(bucket, fileName, imageFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String uploadVideo(MultipartFile videoFile) throws IOException
    {
        String fileName = createRandomFileName(videoFile);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(videoFile.getContentType());

        amazonS3Client.putObject(bucket, fileName, videoFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String createRandomFileName(MultipartFile file)
    {
        return folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    }

}
