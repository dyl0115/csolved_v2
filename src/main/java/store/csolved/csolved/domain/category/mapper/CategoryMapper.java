package store.csolved.csolved.domain.category.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.category.dto.Category;

import java.util.List;

@Mapper
public interface CategoryMapper
{
    List<Category> getAll(Long postTypeCode);
}
