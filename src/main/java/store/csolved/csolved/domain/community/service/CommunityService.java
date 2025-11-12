package store.csolved.csolved.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.csolved.csolved.domain.answer.mapper.record.AnswerDetailRecord;
import store.csolved.csolved.domain.answer.service.result.AnswerWithCommentsResult;
import store.csolved.csolved.domain.answer.mapper.AnswerMapper;
import store.csolved.csolved.domain.bookmark.mapper.BookmarkMapper;
import store.csolved.csolved.domain.comment.mapper.CommentMapper;
import store.csolved.csolved.domain.comment.mapper.record.CommentDetailRecord;
import store.csolved.csolved.domain.community.mapper.param.CommunityCountParam;
import store.csolved.csolved.domain.community.mapper.param.CommunityCreateParam;
import store.csolved.csolved.domain.community.mapper.param.CommunitySearchParam;
import store.csolved.csolved.domain.community.mapper.param.CommunityUpdateParam;
import store.csolved.csolved.domain.community.mapper.record.CommunityRecord;
import store.csolved.csolved.domain.community.mapper.CommunityMapper;
import store.csolved.csolved.domain.community.service.command.CommunityCreateCommand;
import store.csolved.csolved.domain.community.service.command.CommunitySearchCommand;
import store.csolved.csolved.domain.community.service.command.CommunityUpdateCommand;
import store.csolved.csolved.domain.community.service.result.CommunitiesWithPaginationResult;
import store.csolved.csolved.domain.community.service.result.CommunityResult;
import store.csolved.csolved.domain.community.service.result.CommunityWithAnswersAndCommentsResult;
import store.csolved.csolved.domain.tag.service.TagService;
import store.csolved.csolved.global.exception.CsolvedException;
import store.csolved.csolved.global.exception.ExceptionCode;
import store.csolved.csolved.global.utils.page.Pagination;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommunityService
{
    private final CommunityMapper communityMapper;
    private final AnswerMapper answerMapper;
    private final CommentMapper commentMapper;
    private final BookmarkMapper bookmarkMapper;
    private final TagService tagService;


    @Transactional
    public void create(CommunityCreateCommand command)
    {
        CommunityRecord community = CommunityRecord.from(command);
        communityMapper.saveCommunity(CommunityCreateParam.from(command));
        tagService.saveTags(community.getId(), command.getTags());
    }

    @Transactional
    public void update(Long communityId, CommunityUpdateCommand command)
    {
        CommunityRecord community = CommunityRecord.from(command);
        communityMapper.updateCommunity(community.getId(), CommunityUpdateParam.from(command));
        tagService.updateTags(communityId, command.getTags());
    }

    @Transactional
    public void delete(Long postId)
    {
        communityMapper.deleteCommunity(postId);
    }

    public Long countCommunities(CommunitySearchCommand command)
    {
        return communityMapper.countCommunities(CommunityCountParam.from(command));
    }

    public CommunityResult getCommunity(Long communityId)
    {
        CommunityRecord community = communityMapper.getCommunity(communityId);
        return CommunityResult.from(community);
    }

    public CommunityWithAnswersAndCommentsResult getCommunityWithAnswersAndComments(Long userId, Long communityId)
    {
        CommunityRecord community = communityMapper.getCommunity(communityId);
        boolean bookmarked = bookmarkMapper.hasBookmarked(userId, communityId);
        List<AnswerWithCommentsResult> answersWithComments = getAnswersWithComments(communityId);
        return CommunityWithAnswersAndCommentsResult.from(community, bookmarked, answersWithComments);
    }

    public CommunitiesWithPaginationResult getCommunitiesAndPage(CommunitySearchCommand command)
    {
        Pagination pagination = Pagination.from(command.getRequestPageNumber(), countCommunities(command));
        List<CommunityRecord> communities = communityMapper.getCommunities(CommunitySearchParam.from(command, pagination));
        return CommunitiesWithPaginationResult.from(communities, pagination);
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

    private List<AnswerWithCommentsResult> getAnswersWithComments(Long communityId)
    {
        List<AnswerDetailRecord> answers = answerMapper.getAnswers(communityId);
        Map<Long, List<CommentDetailRecord>> answerWithCommentsMap = mapAnswerIdToComments(extractIds(answers));
        return AnswerWithCommentsResult.from(answers, answerWithCommentsMap);
    }

    private Map<Long, List<CommentDetailRecord>> mapAnswerIdToComments(List<Long> answerIds)
    {
        List<CommentDetailRecord> comments = commentMapper.getComments(answerIds);
        return comments.stream()
                .collect(Collectors.groupingBy(CommentDetailRecord::getAnswerId));
    }

    private List<Long> extractIds(List<AnswerDetailRecord> answers)
    {
        return answers.stream()
                .map(AnswerDetailRecord::getId)
                .toList();
    }
}