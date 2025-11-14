package store.postHub.postHub.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.postHub.postHub.domain.category.Category;
import store.postHub.postHub.domain.category.mapper.CategoryMapper;

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
