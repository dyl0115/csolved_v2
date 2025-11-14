package store.postHub.postHub.domain.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import store.postHub.postHub.common.BaseEntity;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity
{
    private String name;

    public static Tag from(String name)
    {
        return Tag.builder()
                .name(name)
                .build();
    }
}
