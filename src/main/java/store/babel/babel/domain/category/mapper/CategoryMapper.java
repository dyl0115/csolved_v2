package store.babel.babel.domain.category.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.category.CategoryResult;

import java.util.List;

@Mapper
public interface CategoryMapper
{
    List<CategoryResult> getAll(Long postTypeCode);
}
