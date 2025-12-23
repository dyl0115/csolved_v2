package store.babel.babel.domain.assistant.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class PostAssistRequest
{
    private String role;
    private Long authorId;
    private String title;
    private List<String> tags;
    private String content;
    private String message;
}
