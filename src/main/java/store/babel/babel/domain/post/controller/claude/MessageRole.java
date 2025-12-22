package store.babel.babel.domain.post.controller.claude;

import lombok.Getter;

@Getter
public enum MessageRole
{
    USER("user"),
    ASSISTANT("assistant");

    private final String value;

    MessageRole(String value)
    {
        this.value = value;
    }

    public boolean matches(String role)
    {
        return this.value.equals(role);
    }
}
