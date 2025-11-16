package store.babel.babel.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import store.babel.babel.domain.post.dto.*;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

@Mapper
public interface PostMapper
{
    void savePost(PostCreateCommand command);

    void updatePost(PostUpdateCommand command);

    // 게시글 개수 조회
    Long countPosts(PostSearchQuery query);

    // 질문글들 조회
    List<PostCard> getPostCards(@Param("query") PostSearchQuery query,
                                @Param("page") Pagination pagination);

    // 질문글 조회
    Post getPost(Long communityId);

    // 논리적으로 게시글을 삭제
    void deletePost(Long communityId);

    // 질문-좋아요 테이블에 저장된 유저인지 체크
    boolean hasUserLiked(Long communityId, Long authorId);

    // 질문 테이블의 Likes 1증가
    void increaseLikes(Long communityId);

    // 질문-좋아요 테이블에 questionId, userId 저장 (중복 좋아요 방지)
    void addUserLike(Long communityId, Long authorId);

    // 질문 테이블의 Views 1증가
    void increaseView(Long communityId);

    // 댓글 단 게시글 리스트 조회
    List<Post> getAnsweredCommunities(@Param("userId") Long userId,
                                      @Param("page") Pagination page);

    // 댓글 단 게시글 개수 조회
    Long countAnsweredCommunities(Long userId);

    // 작성한 게시글 리스트 조회
    List<Post> getUserCommunities(Long userId, Pagination page);

    //    작성한 게시글 개수 조회
    Long countUserCommunities(Long userId);

    //    북마크한 게시글 조회
    List<Post> getBookmarkedCommunities(Long userId, Pagination page);
}
