package store.csolved.csolved.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class FileController
{
    private final FileService fileService;

    @PostMapping("/api/upload-image")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> imageSave(@RequestParam("file") MultipartFile file)
    {
        try
        {
            String imageUrl = fileService.uploadImage(file, "post/free-board");
            HashMap<String, String> response = new HashMap<>();
            response.put("location", imageUrl);
            return ResponseEntity.ok(response);
        }
        catch (IOException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
