package store.csolved.csolved.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
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

    /**
     * Base64로 인코딩된 이미지를 S3에 업로드합니다.
     * @param base64Image Base64 인코딩된 이미지 문자열 (data:image/jpeg;base64, 접두어 포함 가능)
     * @param originalFileName 원본 파일명
     * @return S3에 업로드된 이미지 URL
     */
    public String uploadBase64Image(String base64Image, String originalFileName) throws IOException
    {
        if (base64Image == null || base64Image.isEmpty())
        {
            return null;
        }

        // data:image/jpeg;base64, 접두어 제거
        String base64Data = base64Image;
        String contentType = "image/jpeg";

        if (base64Image.contains(","))
        {
            String[] parts = base64Image.split(",");
            if (parts.length == 2)
            {
                // data:image/jpeg;base64 부분에서 content type 추출
                String header = parts[0];
                if (header.contains(":") && header.contains(";"))
                {
                    contentType = header.substring(header.indexOf(":") + 1, header.indexOf(";"));
                }
                base64Data = parts[1];
            }
        }

        // Base64 디코딩
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        // 파일명 생성
        String fileName = folderName + "/" + UUID.randomUUID() + "_" + originalFileName;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(imageBytes.length);

        // S3 업로드
        amazonS3Client.putObject(bucket, fileName, inputStream, metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String createRandomFileName(MultipartFile file)
    {
        return folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    }

}
