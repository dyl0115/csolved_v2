package store.csolved.csolved.global.llm;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@Component
public class PromptManager
{
    public String load(String filename)
    {
        try
        {
            ClassPathResource resource = new ClassPathResource(filename);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException("프롬프트 파일 로딩 실패: " + filename, e);
        }
    }

    public String loadAndRender(String filename, Object source)
    {
        String template = load(filename);

        try
        {
            for (Field field : source.getClass().getDeclaredFields())
            {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(source);
                template = template.replace("{" + key + "}", String.valueOf(value));
            }
        }
        catch (IllegalAccessException exception)
        {
            throw new RuntimeException("프롬프트 렌더링 실패", exception);
        }

        return template;
    }
}
