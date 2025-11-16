package store.babel.babel.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.babel.babel.common.BaseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity
{
    private String name;

    public static Category create(String name)
    {
        return Category.builder()
                .name(name)
                .build();
    }
}
