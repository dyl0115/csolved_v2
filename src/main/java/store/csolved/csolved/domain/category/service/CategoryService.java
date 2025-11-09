package store.csolved.csolved.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.csolved.csolved.domain.category.Category;
import store.csolved.csolved.domain.category.mapper.CategoryMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryMapper categoryMapper;

    public List<Category> getAllCategories(int postType)
    {
        return categoryMapper.getAll(postType);
    }
}
