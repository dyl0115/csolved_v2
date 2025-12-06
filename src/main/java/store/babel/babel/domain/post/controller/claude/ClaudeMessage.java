package store.babel.babel.domain.post.controller.claude;

import com.anthropic.models.messages.MessageParam;
import lombok.Getter;

import java.util.List;

@Getter
public class ClaudeMessage
{
    private MessageParam.Role role;
    private String title;
    private List<String> tags;
    private String content;
    private String message;
}
