package store.csolved.csolved.config;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig
{
    @Value("${CLAUDE_API_KEY}")
    private String claudeApiKey;

    @Value("${GEMINI_API_KEY}")
    private String googleApiKey;

    @Bean
    public AnthropicClient anthropicClient()
    {
        return AnthropicOkHttpClient.builder()
                .putHeader("anthropic-beta", "structured-outputs-2025-11-13")
                .apiKey(claudeApiKey)
                .build();
    }

    @Bean
    public Client googleClient()
    {
        return Client.builder()
                .apiKey(googleApiKey)
                .build();
    }
}
