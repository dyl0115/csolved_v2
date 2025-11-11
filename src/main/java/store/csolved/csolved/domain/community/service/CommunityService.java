package store.csolved.csolved.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.mapper.entity.Answer;
import store.csolved.csolved.domain.answer.AnswerWithComments;
import store.csolved.csolved.domain.answer.mapper.AnswerMapper;
import store.csolved.csolved.domain.bookmark.service.BookmarkService;
import store.csolved.csolved.domain.comment.Comment;
import store.csolved.csolved.domain.comment.mapper.CommentMapper;
import store.csolved.csolved.domain.community.mapper.entity.Community;
import store.csolved.csolved.domain.community.mapper.CommunityMapper;
import store.csolved.csolved.domain.community.service.command.CommunityCreateCommand;
import store.csolved.csolved.domain.community.service.command.CommunityUpdateCommand;
import store.csolved.csolved.domain.community.service.result.CommunitiesAndPageResult;
import store.csolved.csolved.domain.community.service.result.CommunityResult;
import store.csolved.csolved.domain.community.service.result.CommunityWithAnswersAndCommentsResult;
import store.csolved.csolved.domain.tag.service.TagService;
import store.csolved.csolved.global.exception.CsolvedException;
import store.csolved.csolved.global.exception.ExceptionCode;
import store.csolved.csolved.utils.filter.Filtering;
import store.csolved.csolved.utils.page.Pagination;
import store.csolved.csolved.utils.page.PaginationManager;
import store.csolved.csolved.utils.search.Searching;
import store.csolved.csolved.utils.sort.Sorting;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static store.csolved.csolved.common.PostType.COMMUNITY;

@RequiredArgsConstructor
@Service
public class CommunityService
{
    private final CommunityMapper communityMapper;
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;
    private final BookmarkService bookmarkService;
    private final PaginationManager paginationManager;

    private final TagService tagService;

    @Transactional
    public void create(CommunityCreateCommand command)
    {
        Community post = Community.from(command);
        communityMapper.saveCommunity(COMMUNITY.getCode(), post);
        tagService.saveTags(post.getId(), command.getTags());
    }

    @Transactional
    public void update(Long communityId, CommunityUpdateCommand command)
    {
        Community post = Community.from(command);
        communityMapper.updateCommunity(communityId, post);
        tagService.updateTags(communityId, command.getTags());
    }

    @Transactional
    public void delete(Long postId)
    {
        communityMapper.deleteCommunity(postId);
    }

    public Long countPosts(Filtering filter, Searching search)
    {
        return communityMapper.countCommunities(
                COMMUNITY.getCode(),
                filter.getFilterType(),
                filter.getFilterValue(),
                search.getSearchType(),
                search.getKeyword());
    }

    public CommunityResult getCommunity(Long communityId)
    {
        Community community = communityMapper.getCommunity(communityId);
        return CommunityResult.from(community);
    }

    public CommunityWithAnswersAndCommentsResult getCommunityWithAnswersAndComments(Long userId, Long communityId)
    {
        Community community = communityMapper.getCommunity(communityId);
        boolean bookmarked = bookmarkService.hasBookmarked(userId, communityId);
        List<AnswerWithComments> answersWithComments = getAnswersWithComments(communityId);
        return CommunityWithAnswersAndCommentsResult.from(community, bookmarked, answersWithComments);
    }

    public CommunitiesAndPageResult getPostsAndPage(Long pageNumber,
                                                    Sorting sort,
                                                    Filtering filter,
                                                    Searching search)
    {

        Long totalPage = countPosts(filter, search);

        Pagination page = paginationManager.createPagination(pageNumber, totalPage);

        List<Community> communities = communityMapper.getCommunities(
                COMMUNITY.getCode(),
                page.getOffset(),
                page.getSize(),
                sort.name(),
                filter.getFilterType(),
                filter.getFilterValue(),
                search.getSearchType(),
                search.getKeyword());

        return CommunitiesAndPageResult.from(communities, page);
    }

    @Transactional
    public Community viewCommunity(Long communityId)
    {
        communityMapper.increaseView(communityId);
        return communityMapper.getCommunity(communityId);
    }

    @Transactional
    public void addLike(Long communityId, Long userId)
    {
        if (communityMapper.hasUserLiked(communityId, userId))
        {
            throw new CsolvedException(ExceptionCode.ALREADY_LIKED);
        }

        communityMapper.addUserLike(communityId, userId);
        communityMapper.increaseLikes(communityId);
    }

    private List<AnswerWithComments> getAnswersWithComments(Long communityId)
    {
        List<Answer> answers = answerMapper.getAnswers(communityId);
        Map<Long, List<Comment>> answerWithCommentsMap = mapCommentsToAnswer(extractIds(answers));
        return AnswerWithComments.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<Comment>> mapCommentsToAnswer(List<Long> answerIds)
    {
        List<Comment> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(Comment::getAnswerId));
    }

    private List<Long> extractIds(List<Answer> answers)
    {
        return answers.stream()
                .map(Answer::getId)
                .toList();
    }
}