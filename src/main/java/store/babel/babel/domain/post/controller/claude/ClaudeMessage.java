package store.babel.babel.domain.post.controller.claude;

import com.anthropic.models.messages.MessageParam;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ClaudeMessage
{
    private String role;
    private String title;
    private List<String> tags;
    private String content;
    private String message;
}
