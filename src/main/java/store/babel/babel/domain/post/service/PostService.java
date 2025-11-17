package store.babel.babel.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.post.dto.*;
import store.babel.babel.domain.post.mapper.PostMapper;
import store.babel.babel.domain.tag.service.TagService;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService
{
    private final PostMapper postMapper;
    private final TagService tagService;

    @Transactional
    public void createPost(PostCreateCommand command)
    {
        postMapper.savePost(command);
        tagService.saveTags(command.getId(), command.getTags());
    }

    @Transactional
    public void updatePost(PostUpdateCommand command)
    {
        postMapper.updatePost(command);
        tagService.updateTags(command.getId(), command.getTags());
    }

    @Transactional
    public void deletePost(Long postId)
    {
        postMapper.deletePost(postId);
    }

    public Long countPosts(PostSearchQuery query)
    {
        return postMapper.countPosts(query);
    }

    public Long countAnsweredPosts(Long userId)
    {
        return postMapper.countAnsweredPosts(userId);
    }

    public Long countUserPosts(Long userId)
    {
        return postMapper.countUserPosts(userId);
    }

    public Post getPost(Long postId, Long userId)
    {
        return postMapper.getPost(postId, userId).stringifyTags();
    }

    public List<PostCard> getBookmarkedPostCards(Long userId, Pagination pagination)
    {
        return postMapper.getBookmarkedPosts(userId, pagination);
    }

    public List<PostCard> getAnsweredPostCards(Long userId, Pagination pagination)
    {
        return postMapper.getAnsweredPosts(userId, pagination);
    }

    public List<PostCard> getUserPostCards(Long userId, Pagination pagination)
    {
        return postMapper.getUserPosts(userId, pagination);
    }

    public List<PostCard> getPostCards(PostSearchQuery query, Pagination pagination)
    {
        return postMapper.getPostCards(query, pagination);
    }

    @Transactional
    public void increaseView(Long postId)
    {
        postMapper.increaseView(postId);
    }

    @Transactional
    public void addLike(Long postId, Long userId)
    {
        if (postMapper.hasUserLiked(postId, userId))
        {
            throw new BabelException(ExceptionCode.ALREADY_LIKED);
        }

        postMapper.addUserLike(postId, userId);
        postMapper.increaseLikes(postId);
    }
}