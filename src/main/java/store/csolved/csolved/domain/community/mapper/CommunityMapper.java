package store.csolved.csolved.domain.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import store.csolved.csolved.domain.community.mapper.entity.Community;

import java.util.List;

@Mapper
public interface CommunityMapper
{
    void saveCommunity(int postType, Community community);

    void updateCommunity(Long communityId, Community community);

    // 질문글들 조회
    List<Community> getCommunities(int postType,
                                   Long offset,
                                   Long size,
                                   String sortType,
                                   String filterType,
                                   Long filterValue,
                                   String searchType,
                                   String searchKeyword);

    // 질문글 조회
    Community getCommunity(Long communityId);

    // 논리적으로 게시글을 삭제
    void deleteCommunity(Long communityId);

    // 게시글 개수 조회
    Long countCommunities(int postType,
                          String filterType,
                          Long filterValue,
                          String searchType,
                          String searchKeyword);

    // 질문-좋아요 테이블에 저장된 유저인지 체크
    boolean hasUserLiked(Long communityId, Long authorId);

    // 질문 테이블의 Likes 1증가
    void increaseLikes(Long communityId);

    // 질문-좋아요 테이블에 questionId, userId 저장 (중복 좋아요 방지)
    void addUserLike(Long communityId, Long authorId);

    // 질문 테이블의 Views 1증가
    void increaseView(Long communityId);
}
