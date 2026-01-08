package store.csolved.csolved.domain.assistant.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class PostAssistResponse
{
    private String role;
    private String title;
    private List<String> tags;
    private String content;
    private String message;
}
