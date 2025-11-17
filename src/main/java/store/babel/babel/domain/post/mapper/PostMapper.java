package store.babel.babel.domain.post.mapper;

import org.apache.ibatis.annotations.Mapper;
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

    // 게시글들 조회
    List<PostCard> getPostCards(PostSearchQuery query, Pagination pagination);

    // 게시글 조회
    Post getPost(Long postId, Long userId);

    // 게시글 논리적 삭제
    void deletePost(Long postId);

    // 질문-좋아요 테이블에 저장된 유저인지 체크
    boolean hasUserLiked(Long postId, Long authorId);

    // 질문 테이블의 Likes 1증가
    void increaseLikes(Long postId);

    // 질문-좋아요 테이블에 questionId, userId 저장 (중복 좋아요 방지)
    void addUserLike(Long postId, Long authorId);

    // 질문 테이블의 Views 1증가
    void increaseView(Long postId);

    // 댓글 단 게시글 리스트 조회
    List<PostCard> getAnsweredPosts(Long userId, Pagination pagination);

    // 댓글 단 게시글 개수 조회
    Long countAnsweredPosts(Long userId);

    // 작성한 게시글 리스트 조회
    List<PostCard> getUserPosts(Long userId, Pagination pagination);

    //    작성한 게시글 개수 조회
    Long countUserPosts(Long userId);

    //    북마크한 게시글 조회
    List<PostCard> getBookmarkedPosts(Long userId, Pagination pagination);
}
