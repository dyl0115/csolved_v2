package store.babel.babel.config;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.babel.babel.domain.post.controller.claude.ClaudeSessionManager;

@Configuration
public class ClaudeConfig
{
    @Value("${CLAUDE_API_KEY")
    private String claudeApiKey;

    @Bean
    public AnthropicClient anthropicClient()
    {
        return AnthropicOkHttpClient.builder()
                .apiKey(claudeApiKey)
                .build();
    }
}
