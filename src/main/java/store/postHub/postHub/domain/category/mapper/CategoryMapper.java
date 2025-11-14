package store.postHub.postHub.domain.category.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.postHub.postHub.domain.category.Category;

import java.util.List;

@Mapper
public interface CategoryMapper
{
    List<Category> getAll(Long postTypeCode);
}
