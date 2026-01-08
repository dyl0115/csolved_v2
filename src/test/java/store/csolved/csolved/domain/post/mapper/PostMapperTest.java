package store.csolved.csolved.domain.post.mapper;

import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class PostMapperTest
{
//    @Autowired
//    private PostMapper postMapper;
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @BeforeAll
//    void setUp()
//    {
//        // FK 제약조건을 지키기 위해 미리 필요한 데이터를 저장.
//        // PostMapper 테스트에 다른 Mapper에 의존하지 않도록 한다.
//
//        // 사용자 데이터
//        jdbcTemplate.execute(
//                "INSERT INTO users (id, email, password, nickname, company, admin, created_at)\n" +
//                        "VALUES (1, 'test1@example.com', 'password123', 'testUser1', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
//                        "       (2, 'test2@example.com', 'password123', 'testUser2', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
//                        "       (3, 'admin@example.com', 'password123', 'adminUser', 'testCompany', true, CURRENT_TIMESTAMP);");
//
//        // 카테고리 데이터
//        jdbcTemplate.execute(
//                "INSERT INTO category (id, post_type, name, created_at)\n" +
//                        "VALUES (1, 1, '일반질문', CURRENT_TIMESTAMP),\n" +
//                        "       (2, 1, '기술토론', CURRENT_TIMESTAMP),\n" +
//                        "       (3, 2, '커뮤니티', CURRENT_TIMESTAMP);");
//
//        // 태그 데이터
//        jdbcTemplate.execute(
//                "INSERT INTO tags (id, name, created_at)\n" +
//                        "VALUES (1, 'Java', CURRENT_TIMESTAMP),\n" +
//                        "       (2, 'Spring', CURRENT_TIMESTAMP),\n" +
//                        "       (3, 'MySQL', CURRENT_TIMESTAMP),\n" +
//                        "       (4, 'Test', CURRENT_TIMESTAMP);");
//    }
//
//    @Test
//    @DisplayName("게시글을 저장하고 해당 게시글이 잘 조회되는지 확인")
//    void savePost()
//    {
//        // given
//        Long authorId = 1L;
//        String title = "테스트 게시글";
//        String content = "테스트 내용";
//
//        PostCreateCommand command = PostCreateCommand.builder()
//                .postType(1L)
//                .title(title)
//                .content(content)
//                .authorId(authorId)
//                .anonymous(false)
//                .categoryId(1L)
//                .tags(List.of(Tag.builder().id(1L).name("Java").build()))
//                .build();
//
//        // when
//        postMapper.savePost(command);
//
//        // then
//        Post post = postMapper.getPost(command.getId(), authorId);
//
//        assertThat(command.getId()).isNotNull();
//        assertThat(command.getId()).isGreaterThan(0L);
//
//        assertThat(post.getTitle()).isEqualTo(title);
//        assertThat(post.getContent()).isEqualTo(content);
//    }
//
//    @Test
//    @DisplayName("게시글을 수정하고 변경사항이 반영되는지 확인")
//    void updatePost()
//    {
//        // given
//        PostCreateCommand createCommand = PostCreateCommand.builder()
//                .postType(1L)
//                .title("제목")
//                .content("내용")
//                .authorId(1L)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Java").build()))
//                .build();
//
//        postMapper.savePost(createCommand);
//
//
//        // when
//        PostUpdateCommand updateCommand = PostUpdateCommand.builder()
//                .id(createCommand.getId())
//                .title("수정된 제목")
//                .content("수정된 내용")
//                .authorId(1L)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Spring").build()))
//                .build();
//
//        postMapper.updatePost(updateCommand);
//
//        // then
//        Post updatedPost = postMapper.getPost(createCommand.getId(), 1L);
//        assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목");
//        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");
//        assertThat(updatedPost.isAnonymous()).isTrue();
//        assertThat(updatedPost.getCategoryId()).isEqualTo(2L);
//    }
//
//    @Test
//    @DisplayName("게시글을 논리적으로 삭제하고 조회되지 않는지 확인")
//    void deletePost()
//    {
//        // given
//        PostCreateCommand command = PostCreateCommand.builder()
//                .postType(1L)
//                .title("제목")
//                .content("내용")
//                .authorId(1L)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Java").build()))
//                .build();
//
//        postMapper.savePost(command);
//
//        // TODO 이후 테이블 테이블 스키마 변경 후 작성
//        // when
////        postMapper.deletePost(command.getId());
//
//        // then
////        Post deletedPost = postMapper.getPost(command.getId(), 1L);
////        assertThat(deletedPost).isNull();
//    }
//
//    @Test
//    @DisplayName("검색 조건에 맞는 게시글 목록을 조회")
//    void getPostCards()
//    {
//        Long davidId = 1L;
//        Long alexId = 3L;
//
//        // given
//        PostCreateCommand command1 = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("David-1")
//                .content("David-1")
//                .authorId(davidId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Java").build()))
//                .build();
//
//        PostCreateCommand command2 = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("David-2")
//                .content("David-2")
//                .authorId(davidId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Java").build()))
//                .build();
//
//        PostCreateCommand command3 = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("David-3")
//                .content("David-3")
//                .authorId(davidId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Java").build()))
//                .build();
//
//        PostCreateCommand command4 = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("Alex-1")
//                .content("Alex-1")
//                .authorId(alexId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Java").build()))
//                .build();
//
//        postMapper.savePost(command1);
//        postMapper.savePost(command2);
//        postMapper.savePost(command3);
//        postMapper.savePost(command4);
//
//        // when
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .build();
//
//        PostCountQuery query = PostCountQuery.from(request);
//        Long postCount = postMapper.countPosts(query);
//        Pagination pagination = Pagination.from(1L, postCount);
//
//        PostSearchQuery searchQuery = PostSearchQuery.from(request, pagination);
//        List<PostCard> postCards = postMapper.getPostCards(searchQuery);
//
//        // then
//        assertThat(postCards.size()).isEqualTo(4);
//        assertThat(postCards.get(0).getAuthorId()).isEqualTo(davidId);
//        assertThat(postCards.get(1).getAuthorId()).isEqualTo(davidId);
//        assertThat(postCards.get(2).getAuthorId()).isEqualTo(davidId);
//    }
//
//    @Test
//    @DisplayName("제목으로 게시글을 검색하고 결과를 확인")
//    void getPostCardsByTitle()
//    {
//        // given
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .categoryId(1L)
//                .searchType(TITLE)
//                .searchKeyword("테스트")
//                .sortType(RECENT)
//                .page(1L)
//                .build();
//
//        Pagination pagination = Pagination.from(1L, 10L);
//        PostSearchQuery query = PostSearchQuery.from(request, pagination);
//
//        // when
//        List<PostCard> postCards = postMapper.getPostCards(query);
//
//        // then
//        assertThat(postCards).isNotNull();
//        postCards.forEach(card ->
//                assertThat(card.getTitle()).contains("테스트")
//        );
//    }
//
//    @Test
//    @DisplayName("카테고리별로 게시글을 필터링하고 결과를 확인")
//    void getPostCardsByCategory()
//    {
//        // given
//        Long categoryId = 1L;
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .sortType(RECENT)
//                .categoryId(1L)
//                .searchType(TITLE)
//                .searchKeyword("")
//                .build();
//
//        Pagination pagination = Pagination.from(1L, 10L);
//
//        PostSearchQuery query = PostSearchQuery.from(request, pagination);
//
//        // when
//        List<PostCard> postCards = postMapper.getPostCards(query);
//
//        // then
//        assertThat(postCards).isNotNull();
//        postCards.forEach(card ->
//                assertThat(card.getCategoryId()).isEqualTo(categoryId)
//        );
//    }
//
//    @Test
//    @DisplayName("조회수 순으로 게시글을 정렬하고 결과를 확인")
//    void getPostCardsSortedByViews()
//    {
//        // given
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .sortType(VIEWS)
//                .categoryId(1L)
//                .searchType(TITLE)
//                .searchKeyword("")
//                .build();
//
//        Pagination pagination = Pagination.from(1L, 10L);
//        PostSearchQuery query = PostSearchQuery.from(request, pagination);
//
//        // when
//        List<PostCard> postCards = postMapper.getPostCards(query);
//
//        // then
//        assertThat(postCards).isNotNull();
//        if (postCards.size() > 1)
//        {
//            assertThat(postCards.get(0).getViews())
//                    .isGreaterThanOrEqualTo(postCards.get(1).getViews());
//        }
//    }
//
//    @Test
//    @DisplayName("검색 조건에 맞는 게시글 개수를 정확히 계산")
//    void countPosts()
//    {
//        // given
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .searchType(TITLE)
//                .searchKeyword("")
//                .build();
//
//        PostCountQuery query = PostCountQuery.from(request);
//
//        // when
//        Long count = postMapper.countPosts(query);
//
//        // then
//        assertThat(count).isNotNull();
//        assertThat(count).isGreaterThanOrEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("특정 카테고리의 게시글 개수를 정확히 계산")
//    void countPostsByCategory()
//    {
//        // given
//        Long categoryId = 1L;
//
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .categoryId(categoryId)
//                .searchType(TITLE)
//                .searchKeyword("")
//                .build();
//
//        PostCountQuery query = PostCountQuery.from(request);
//
//        // when
//        Long count = postMapper.countPosts(query);
//
//        // then
//        assertThat(count).isNotNull();
//        assertThat(count).isGreaterThanOrEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("사용자가 좋아요를 누르지 않았을 때 false 반환")
//    void hasUserLiked_whenNotLiked()
//    {
//        // given
//        Long postId = 1L;
//        Long userId = 1L;
//
//        // when
//        boolean hasLiked = postMapper.hasUserLiked(postId, userId);
//
//        // then
//        assertThat(hasLiked).isFalse();
//    }
//
//    @Test
//    @DisplayName("사용자가 좋아요를 추가하고 확인")
//    void addUserLike()
//    {
//        // given
//        Long userId = 1L;
//
//        PostCreateCommand command = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(userId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Spring").build()))
//                .build();
//
//        postMapper.savePost(command);
//
//        // when
//        Long postId = command.getId();
//        postMapper.addUserLike(postId, userId);
//
//        // then
//        boolean hasLiked = postMapper.hasUserLiked(postId, userId);
//        assertThat(hasLiked).isTrue();
//    }
//
//    @Test
//    @DisplayName("게시글의 좋아요 수를 증가시키고 확인")
//    void increaseLikes()
//    {
//        // given
//        Long authorId = 1L;
//
//        PostCreateCommand command = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(authorId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Spring").build()))
//                .build();
//
//        postMapper.savePost(command);
//
//        Long postId = command.getId();
//
//        Post beforePost = postMapper.getPost(postId, authorId);
//        Long beforeLikes = beforePost.getLikes();
//
//        // when
//        postMapper.increaseLikes(postId);
//
//        // then
//        Post afterPost = postMapper.getPost(postId, authorId);
//        assertThat(afterPost.getLikes()).isEqualTo(beforeLikes + 1);
//    }
//
//    @Test
//    @DisplayName("게시글의 조회수를 증가시키고 확인")
//    void increaseView()
//    {
//        // given
//        Long postId;
//        Long authorId = 1L;
//
//        PostCreateCommand command = PostCreateCommand.builder()
//                .postType(POST.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(authorId)
//                .anonymous(true)
//                .categoryId(2L)
//                .tags(List.of(Tag.builder().id(2L).name("Spring").build()))
//                .build();
//
//        postMapper.savePost(command);
//        postId = command.getId();
//
//        Post beforePost = postMapper.getPost(postId, 1L);
//        Long beforeViews = beforePost.getViews();
//
//        // when
//        postMapper.increaseView(postId);
//
//        // then
//        Post afterPost = postMapper.getPost(postId, 1L);
//        assertThat(afterPost.getViews()).isEqualTo(beforeViews + 1);
//    }
//
//    @Test
//    @DisplayName("사용자가 댓글을 단 게시글 목록을 조회")
//    void getAnsweredPosts()
//    {
//        // given
//        Long userId = 1L;
//        Pagination pagination = Pagination.from(1L, 10L);
//
//        // when
//        List<PostCard> answeredPosts = postMapper.getAnsweredPosts(userId, pagination);
//
//        // then
//        assertThat(answeredPosts).isNotNull();
//    }
//
//    @Test
//    @DisplayName("사용자가 댓글을 단 게시글 개수를 정확히 계산")
//    void countAnsweredPosts()
//    {
//        // given
//        Long userId = 1L;
//
//        // when
//        Long count = postMapper.countAnsweredPosts(userId);
//
//        // then
//        assertThat(count).isNotNull();
//        assertThat(count).isGreaterThanOrEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("사용자가 작성한 게시글 목록을 조회")
//    void getUserPosts()
//    {
//        // given
//        Long userId = 1L;
//        Pagination pagination = Pagination.from(1L, 10L);
//
//        // when
//        List<PostCard> userPosts = postMapper.getMyPosts(userId, pagination);
//
//        // then
//        assertThat(userPosts).isNotNull();
//        userPosts.forEach(post ->
//                assertThat(post.getAuthorId()).isEqualTo(userId)
//        );
//    }
//
//    @Test
//    @DisplayName("사용자가 작성한 게시글 개수를 정확히 계산")
//    void countUserPosts()
//    {
//        // given
//        Long userId = 1L;
//
//        // when
//        Long count = postMapper.countMyPosts(userId);
//
//        // then
//        assertThat(count).isNotNull();
//        assertThat(count).isGreaterThanOrEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("사용자가 북마크한 게시글 목록을 조회")
//    void getBookmarkedPosts()
//    {
//        // given
//        Long userId = 1L;
//        Pagination pagination = Pagination.from(1L, 10L);
//
//        // when
//        List<PostCard> bookmarkedPosts = postMapper.getBookmarkedPosts(userId, pagination);
//
//        // then
//        assertThat(bookmarkedPosts).isNotNull();
//    }
//
//    @Test
//    @DisplayName("작성자 닉네임으로 게시글을 검색하고 익명이 아닌 게시글만 조회")
//    void getPostCardsByAuthor()
//    {
//        // given
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .sortType(RECENT)
//                .searchType(AUTHOR)
//                .searchKeyword("테스트")
//                .build();
//
//        Pagination pagination = Pagination.from(1L, 10L);
//        PostSearchQuery query = PostSearchQuery.from(request, pagination);
//
//        // when
//        List<PostCard> postCards = postMapper.getPostCards(query);
//
//        // then
//        assertThat(postCards).isNotNull();
//        postCards.forEach(card ->
//        {
//            assertThat(card.isAnonymous()).isFalse();
//            assertThat(card.getAuthorNickname()).contains("테스트");
//        });
//    }
//
//    @Test
//    @DisplayName("좋아요 순으로 게시글을 정렬하고 결과를 확인")
//    void getPostCardsSortedByLikes()
//    {
//        // given
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .sortType(LIKES)
//                .searchType(TITLE)
//                .searchKeyword("")
//                .build();
//
//        Pagination pagination = Pagination.from(1L, 10L);
//        PostSearchQuery query = PostSearchQuery.from(request, pagination);
//
//        // when
//        List<PostCard> postCards = postMapper.getPostCards(query);
//
//        // then
//        assertThat(postCards).isNotNull();
//        if (postCards.size() > 1)
//        {
//            assertThat(postCards.get(0).getLikes())
//                    .isGreaterThanOrEqualTo(postCards.get(1).getLikes());
//        }
//    }
//
//    @Test
//    @DisplayName("답변 수 순으로 게시글을 정렬하고 결과를 확인")
//    void getPostCardsSortedByAnswers()
//    {
//        //given
//        PostSearchRequest request = PostSearchRequest.builder()
//                .postType(POST)
//                .sortType(ANSWERS)
//                .searchType(TITLE)
//                .searchKeyword("")
//                .build();
//
//        Pagination pagination = Pagination.from(1L, 10L);
//        PostSearchQuery query = PostSearchQuery.from(request, pagination);
//
//        // when
//        List<PostCard> postCards = postMapper.getPostCards(query);
//
//        // then
//        assertThat(postCards).isNotNull();
//        if (postCards.size() > 1)
//        {
//            assertThat(postCards.get(0).getAnswerCount())
//                    .isGreaterThanOrEqualTo(postCards.get(1).getAnswerCount());
//        }
//    }
}
