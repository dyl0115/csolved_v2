package store.csolved.csolved.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import store.csolved.csolved.domain.bookmark.PostCard;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@Mapper
public interface PostMapper
{
    List<PostCard> getRepliedPosts(@Param("userId") Long userId,
                                   @Param("page") Pagination page);

    Long countRepliedPosts(Long userId);

    List<PostCard> getUserPosts(Long userId, Pagination page);

    Long countUserPosts(Long userId);
}
