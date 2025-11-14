package store.babel.babel.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.service.result.AnswerDetailResult;
import store.babel.babel.domain.answer.mapper.AnswerMapper;
import store.babel.babel.domain.bookmark.mapper.BookmarkMapper;
import store.babel.babel.domain.comment.mapper.CommentMapper;
import store.babel.babel.domain.comment.mapper.record.CommentResult;
import store.babel.babel.domain.post.dto.*;
import store.babel.babel.domain.post.mapper.PostMapper;
import store.babel.babel.domain.tag.service.TagService;
import store.babel.babel.global.exception.BabelException;
import store.babel.babel.global.exception.ExceptionCode;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService
{
    private final PostMapper postMapper;
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;
    private final BookmarkMapper bookmarkMapper;
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

    public Long countPosts(PostSearchQuery command)
    {
        return postMapper.countPosts(command);
    }

    public Post getPostDetail(Long postId)
    {
        return postMapper.getPost(postId);
    }

    public PostWithAnswers getPostDetailWithAnswersAndComments(Long userId, Long postId)
    {
        Post post = postMapper.getPost(postId);
        boolean bookmarked = bookmarkMapper.hasBookmarked(userId, postId);
        List<AnswerDetailResult> answersWithComments = getAnswersWithComments(postId);
        return PostWithAnswers.from(post, bookmarked, answersWithComments);
    }

    public PostCardWithPage getPostCardsAndPage(PostSearchQuery command)
    {
        Pagination pagination = Pagination.from(command.getPageNumber(), countPosts(command));
        List<PostCard> posts = postMapper.getPostCards(command, pagination);
        return PostCardWithPage.from(posts, pagination);
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

    private List<AnswerDetailResult> getAnswersWithComments(Long postId)
    {
        List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answers = answerMapper.getAnswers(postId);
        Map<Long, List<CommentResult>> answerWithCommentsMap = mapAnswerIdToComments(extractIds(answers));
        return AnswerDetailResult.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<CommentResult>> mapAnswerIdToComments(List<Long> answerIds)
    {
        List<CommentResult> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(CommentResult::getAnswerId));
    }

    private List<Long> extractIds(List<store.babel.babel.domain.answer.mapper.record.AnswerDetailResult> answers)
    {
        return answers.stream()
                .map(store.babel.babel.domain.answer.mapper.record.AnswerDetailResult::getId)
                .toList();
    }
}