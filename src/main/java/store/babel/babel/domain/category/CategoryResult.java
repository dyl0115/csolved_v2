package store.babel.babel.domain.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.babel.babel.common.BaseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResult extends BaseEntity
{
    private String name;

    public static CategoryResult create(String name)
    {
        return CategoryResult.builder()
                .name(name)
                .build();
    }
}
