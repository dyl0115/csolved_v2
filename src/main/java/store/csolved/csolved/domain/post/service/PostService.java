package store.csolved.csolved.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.csolved.csolved.domain.bookmark.PostCard;
import store.csolved.csolved.domain.post.mapper.PostMapper;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService
{
    private final PostMapper postMapper;

    public List<PostCard> getRepliedPosts(Long userId, Pagination page)
    {
        return postMapper.getRepliedPosts(userId, page);
    }

    public Long countRepliedPosts(Long userId)
    {
        return postMapper.countRepliedPosts(userId);
    }

    public List<PostCard> getUserPosts(Long userId, Pagination page)
    {
        return postMapper.getUserPosts(userId, page);
    }

    public Long countUserPosts(Long userId)
    {
        return postMapper.countUserPosts(userId);
    }
}
