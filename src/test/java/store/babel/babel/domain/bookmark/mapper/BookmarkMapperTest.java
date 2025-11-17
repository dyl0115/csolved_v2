package store.babel.babel.domain.bookmark.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class BookmarkMapperTest
{
    @Autowired
    private BookmarkMapper bookmarkMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp()
    {
        // FK 제약조건을 지키기 위해 미리 필요한 데이터를 저장.
        // BookmarkMapper 테스트에 다른 Mapper에 의존하지 않도록 한다.

        // 사용자 데이터
        jdbcTemplate.execute(
                "INSERT INTO users (id, email, password, nickname, company, admin, created_at)\n" +
                        "VALUES (1, 'test1@example.com', 'password123', 'testUser1', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
                        "       (2, 'test2@example.com', 'password123', 'testUser2', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
                        "       (3, 'test3@example.com', 'password123', 'testUser3', 'testCompany', false, CURRENT_TIMESTAMP);");

        // 게시글 데이터 (북마크할 게시글)
        jdbcTemplate.execute(
                "INSERT INTO posts (id, post_type, user_id, anonymous, title, content, created_at)\n" +
                        "VALUES (1, 1, 1, false, '테스트 게시글1', '테스트 내용1', CURRENT_TIMESTAMP),\n" +
                        "       (2, 1, 1, false, '테스트 게시글2', '테스트 내용2', CURRENT_TIMESTAMP),\n" +
                        "       (3, 1, 2, false, '테스트 게시글3', '테스트 내용3', CURRENT_TIMESTAMP);");
    }

    @Test
    @DisplayName("북마크를 저장하고 저장 여부를 확인")
    void saveBookmark()
    {
        // given
        Long userId = 1L;
        Long postId = 1L;

        // when
        bookmarkMapper.saveBookmark(userId, postId);

        // then
        boolean hasBookmarked = bookmarkMapper.hasBookmarked(userId, postId);
        assertThat(hasBookmarked).isTrue();
    }

    @Test
    @DisplayName("북마크를 삭제하고 삭제 여부를 확인")
    void deleteBookmark()
    {
        // given
        Long userId = 1L;
        Long postId = 1L;

        bookmarkMapper.saveBookmark(userId, postId);

        // when
        bookmarkMapper.deleteBookmark(userId, postId);

        // then
        boolean hasBookmarked = bookmarkMapper.hasBookmarked(userId, postId);
        assertThat(hasBookmarked).isFalse();
    }

    @Test
    @DisplayName("사용자의 북마크 개수를 정확히 계산")
    void countBookmarks()
    {
        // given
        Long userId = 1L;

        bookmarkMapper.saveBookmark(userId, 1L);
        bookmarkMapper.saveBookmark(userId, 2L);
        bookmarkMapper.saveBookmark(userId, 3L);

        // when
        Long count = bookmarkMapper.countBookmarks(userId);

        // then
        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("북마크하지 않은 게시글은 false 반환")
    void hasBookmarked_whenNotBookmarked()
    {
        // given
        Long userId = 1L;
        Long postId = 1L;

        // when
        boolean hasBookmarked = bookmarkMapper.hasBookmarked(userId, postId);

        // then
        assertThat(hasBookmarked).isFalse();
    }

    @Test
    @DisplayName("북마크한 게시글은 true 반환")
    void hasBookmarked_whenBookmarked()
    {
        // given
        Long userId = 1L;
        Long postId = 1L;

        bookmarkMapper.saveBookmark(userId, postId);

        // when
        boolean hasBookmarked = bookmarkMapper.hasBookmarked(userId, postId);

        // then
        assertThat(hasBookmarked).isTrue();
    }

    @Test
    @DisplayName("여러 사용자가 같은 게시글을 북마크")
    void multipleUsersBookmarkSamePost()
    {
        // given
        Long postId = 1L;

        bookmarkMapper.saveBookmark(1L, postId);
        bookmarkMapper.saveBookmark(2L, postId);
        bookmarkMapper.saveBookmark(3L, postId);

        // when & then
        assertThat(bookmarkMapper.hasBookmarked(1L, postId)).isTrue();
        assertThat(bookmarkMapper.hasBookmarked(2L, postId)).isTrue();
        assertThat(bookmarkMapper.hasBookmarked(3L, postId)).isTrue();
    }

    @Test
    @DisplayName("한 사용자가 여러 게시글을 북마크")
    void singleUserBookmarksMultiplePosts()
    {
        // given
        Long userId = 1L;

        bookmarkMapper.saveBookmark(userId, 1L);
        bookmarkMapper.saveBookmark(userId, 2L);
        bookmarkMapper.saveBookmark(userId, 3L);

        // when & then
        assertThat(bookmarkMapper.hasBookmarked(userId, 1L)).isTrue();
        assertThat(bookmarkMapper.hasBookmarked(userId, 2L)).isTrue();
        assertThat(bookmarkMapper.hasBookmarked(userId, 3L)).isTrue();
        assertThat(bookmarkMapper.countBookmarks(userId)).isEqualTo(3L);
    }

    @Test
    @DisplayName("북마크 삭제 후 개수가 감소하는지 확인")
    void countBookmarksAfterDelete()
    {
        // given
        Long userId = 1L;

        bookmarkMapper.saveBookmark(userId, 1L);
        bookmarkMapper.saveBookmark(userId, 2L);
        bookmarkMapper.saveBookmark(userId, 3L);

        Long beforeCount = bookmarkMapper.countBookmarks(userId);

        // when
        bookmarkMapper.deleteBookmark(userId, 2L);

        // then
        Long afterCount = bookmarkMapper.countBookmarks(userId);
        assertThat(afterCount).isEqualTo(beforeCount - 1);
        assertThat(bookmarkMapper.hasBookmarked(userId, 2L)).isFalse();
    }

    @Test
    @DisplayName("북마크가 없는 사용자의 개수는 0")
    void countBookmarks_whenNoBookmarks()
    {
        // given
        Long userId = 1L;

        // when
        Long count = bookmarkMapper.countBookmarks(userId);

        // then
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("다른 사용자의 북마크 삭제는 영향을 주지 않음")
    void deleteBookmark_doesNotAffectOtherUsers()
    {
        // given
        Long postId = 1L;

        bookmarkMapper.saveBookmark(1L, postId);
        bookmarkMapper.saveBookmark(2L, postId);

        // when
        bookmarkMapper.deleteBookmark(1L, postId);

        // then
        assertThat(bookmarkMapper.hasBookmarked(1L, postId)).isFalse();
        assertThat(bookmarkMapper.hasBookmarked(2L, postId)).isTrue();
    }
}
