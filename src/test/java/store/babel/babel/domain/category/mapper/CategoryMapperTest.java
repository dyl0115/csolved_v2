package store.babel.babel.domain.category.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.category.dto.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class CategoryMapperTest
{
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp()
    {
        // 카테고리 데이터
        // post_type: 1 = 일반 게시글, 2 = 커뮤니티, 3 = 공지사항 등
        jdbcTemplate.execute(
                "INSERT INTO category (id, post_type, name, created_at)\n" +
                        "VALUES (1, 1, '일반질문', CURRENT_TIMESTAMP),\n" +
                        "       (2, 1, '기술토론', CURRENT_TIMESTAMP),\n" +
                        "       (3, 1, '코드리뷰', CURRENT_TIMESTAMP),\n" +
                        "       (4, 2, '자유게시판', CURRENT_TIMESTAMP),\n" +
                        "       (5, 2, '유머', CURRENT_TIMESTAMP),\n" +
                        "       (6, 3, '공지', CURRENT_TIMESTAMP);");

        // 삭제된 카테고리 (deleted_at이 설정됨)
        jdbcTemplate.execute(
                "INSERT INTO category (id, post_type, name, created_at, deleted_at)\n" +
                        "VALUES (7, 1, '삭제된카테고리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);");
    }

    @Test
    @DisplayName("특정 게시글 타입의 모든 카테고리를 조회")
    void getAll()
    {
        // given
        Long postType = 1L;

        // when
        List<Category> categories = categoryMapper.getAll(postType);

        // then
        assertThat(categories).isNotNull();
        assertThat(categories.size()).isEqualTo(3);
        assertThat(categories).anyMatch(c -> c.getName().equals("일반질문"));
        assertThat(categories).anyMatch(c -> c.getName().equals("기술토론"));
        assertThat(categories).anyMatch(c -> c.getName().equals("코드리뷰"));
    }

    @Test
    @DisplayName("다른 게시글 타입의 카테고리를 조회")
    void getAll_differentPostType()
    {
        // given
        Long postType = 2L;

        // when
        List<Category> categories = categoryMapper.getAll(postType);

        // then
        assertThat(categories).isNotNull();
        assertThat(categories.size()).isEqualTo(2);
        assertThat(categories).anyMatch(c -> c.getName().equals("자유게시판"));
        assertThat(categories).anyMatch(c -> c.getName().equals("유머"));
    }

    @Test
    @DisplayName("삭제된 카테고리는 조회되지 않음")
    void getAll_excludesDeletedCategories()
    {
        // given
        Long postType = 1L;

        // when
        List<Category> categories = categoryMapper.getAll(postType);

        // then
        assertThat(categories).isNotNull();
        assertThat(categories).noneMatch(c -> c.getName().equals("삭제된카테고리"));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 타입은 빈 리스트 반환")
    void getAll_nonExistentPostType()
    {
        // given
        Long postType = 999L;

        // when
        List<Category> categories = categoryMapper.getAll(postType);

        // then
        assertThat(categories).isNotNull();
        assertThat(categories).isEmpty();
    }

    @Test
    @DisplayName("조회된 카테고리는 ID와 이름을 포함")
    void getAll_containsIdAndName()
    {
        // given
        Long postType = 1L;

        // when
        List<Category> categories = categoryMapper.getAll(postType);

        // then
        assertThat(categories).isNotNull();
        categories.forEach(category -> {
            assertThat(category.getId()).isNotNull();
            assertThat(category.getName()).isNotNull();
            assertThat(category.getCreatedAt()).isNotNull();
        });
    }

    @Test
    @DisplayName("여러 게시글 타입의 카테고리가 혼재되어도 정확히 필터링")
    void getAll_correctlyFiltersByPostType()
    {
        // given & when
        List<Category> type1Categories = categoryMapper.getAll(1L);
        List<Category> type2Categories = categoryMapper.getAll(2L);
        List<Category> type3Categories = categoryMapper.getAll(3L);

        // then
        assertThat(type1Categories.size()).isEqualTo(3);
        assertThat(type2Categories.size()).isEqualTo(2);
        assertThat(type3Categories.size()).isEqualTo(1);

        // 각 타입의 카테고리가 다른 타입과 섞이지 않음
        assertThat(type1Categories).noneMatch(c -> c.getName().equals("자유게시판"));
        assertThat(type2Categories).noneMatch(c -> c.getName().equals("일반질문"));
        assertThat(type3Categories).noneMatch(c -> c.getName().equals("기술토론"));
    }
}
