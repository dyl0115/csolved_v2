package store.babel.babel.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.babel.babel.domain.category.dto.Category;
import store.babel.babel.domain.category.mapper.CategoryMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService
{
    private final CategoryMapper categoryMapper;

    public List<Category> getAllCategories(Long postType)
    {
        return categoryMapper.getAll(postType);
    }
}
