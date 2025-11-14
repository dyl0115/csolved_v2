package store.babel.babel.domain.tag.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.babel.babel.domain.tag.Tag;

import java.util.List;

@Mapper
public interface TagMapper
{
    void saveTags(List<Tag> tags);

    void savePostTags(Long postId, List<Tag> tags);

    List<Tag> getTagsByNames(List<String> tagNames);

    void deleteTag(Long postId);
}
