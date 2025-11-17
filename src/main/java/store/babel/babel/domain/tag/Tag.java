package store.babel.babel.domain.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Tag
{
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static Tag from(String name)
    {
        return Tag.builder()
                .name(name)
                .build();
    }
}
