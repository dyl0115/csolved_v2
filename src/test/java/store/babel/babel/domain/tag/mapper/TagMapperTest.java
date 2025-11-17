package store.babel.babel.domain.tag.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.tag.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class TagMapperTest
{
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp()
    {
        // FK 제약조건을 지키기 위해 미리 필요한 데이터를 저장.
        // TagMapper 테스트에 다른 Mapper에 의존하지 않도록 한다.

        // 사용자 데이터
        jdbcTemplate.execute(
                "INSERT INTO users (id, email, password, nickname, company, admin, created_at)\n" +
                        "VALUES (1, 'test1@example.com', 'password123', 'testUser1', 'testCompany', false, CURRENT_TIMESTAMP);");

        // 게시글 데이터 (태그가 달릴 게시글)
        jdbcTemplate.execute(
                "INSERT INTO posts (id, post_type, user_id, anonymous, title, content, created_at)\n" +
                        "VALUES (1, 1, 1, false, '테스트 게시글1', '테스트 내용1', CURRENT_TIMESTAMP),\n" +
                        "       (2, 1, 1, false, '테스트 게시글2', '테스트 내용2', CURRENT_TIMESTAMP);");

        // 기존 태그 데이터
        jdbcTemplate.execute(
                "INSERT INTO tags (name) " +
                        "VALUES ('Java'), ('Spring'), ('MySQL')");

    }

    @Test
    @DisplayName("새로운 태그들을 저장하고 ID가 자동 생성되는지 확인")
    void saveTags()
    {
        // given
        List<Tag> tags = List.of(
                Tag.from("React"),
                Tag.from("Vue"),
                Tag.from("Angular")
        );

        // when
        tagMapper.saveTags(tags);

        // then
        tags.forEach(tag ->
        {
            assertThat(tag.getId()).isNotNull();
            assertThat(tag.getId()).isGreaterThan(0L);
        });
    }

    @Test
    @DisplayName("게시글에 태그를 연결")
    void savePostTags()
    {
        // given
        Long postId = 1L;
        List<Tag> tags = List.of(
                Tag.builder().id(1L).name("Java").build(),
                Tag.builder().id(2L).name("Spring").build()
        );

        // when
        tagMapper.savePostTags(postId, tags);

        // then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE post_id = ?",
                Integer.class,
                postId
        );
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("태그 이름으로 태그 목록을 조회")
    void getTagsByNames()
    {
        // given
        List<String> tagNames = List.of("Java", "Spring", "MySQL");

        // when
        List<Tag> tags = tagMapper.getTagsByNames(tagNames);

        // then
        assertThat(tags).isNotNull();
        assertThat(tags.size()).isEqualTo(3);
        assertThat(tags).anyMatch(t -> t.getName().equals("Java"));
        assertThat(tags).anyMatch(t -> t.getName().equals("Spring"));
        assertThat(tags).anyMatch(t -> t.getName().equals("MySQL"));
    }

    @Test
    @DisplayName("존재하지 않는 태그 이름으로 조회하면 빈 리스트 반환")
    void getTagsByNames_nonExistentTags()
    {
        // given
        List<String> tagNames = List.of("NonExistent1", "NonExistent2");

        // when
        List<Tag> tags = tagMapper.getTagsByNames(tagNames);

        // then
        assertThat(tags).isNotNull();
        assertThat(tags).isEmpty();
    }

    @Test
    @DisplayName("일부만 존재하는 태그 이름으로 조회하면 존재하는 것만 반환")
    void getTagsByNames_partialMatch()
    {
        // given
        List<String> tagNames = List.of("Java", "Spring", "NonExistent");

        // when
        List<Tag> tags = tagMapper.getTagsByNames(tagNames);

        // then
        assertThat(tags).isNotNull();
        assertThat(tags.size()).isEqualTo(2);
        assertThat(tags).anyMatch(t -> t.getName().equals("Java"));
        assertThat(tags).anyMatch(t -> t.getName().equals("Spring"));
        assertThat(tags).noneMatch(t -> t.getName().equals("NonExistent"));
    }

    @Test
    @DisplayName("게시글의 태그 연결을 삭제")
    void deleteTag()
    {
        // given
        Long postId = 1L;
        List<Tag> tags = List.of(
                Tag.builder().id(1L).name("Java").build(),
                Tag.builder().id(2L).name("Spring").build()
        );

        tagMapper.savePostTags(postId, tags);

        // when
        tagMapper.deleteTag(postId);

        // then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE post_id = ?",
                Integer.class,
                postId
        );
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("태그 삭제는 해당 게시글의 태그만 삭제하고 다른 게시글은 영향 없음")
    void deleteTag_doesNotAffectOtherPosts()
    {
        // given
        Long postId1 = 1L;
        Long postId2 = 2L;

        List<Tag> tags1 = List.of(
                Tag.builder().id(1L).name("Java").build()
        );

        List<Tag> tags2 = List.of(
                Tag.builder().id(2L).name("Spring").build()
        );

        tagMapper.savePostTags(postId1, tags1);
        tagMapper.savePostTags(postId2, tags2);

        // when
        tagMapper.deleteTag(postId1);

        // then
        Integer count1 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE post_id = ?",
                Integer.class,
                postId1
        );
        Integer count2 = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE post_id = ?",
                Integer.class,
                postId2
        );

        assertThat(count1).isEqualTo(0);
        assertThat(count2).isEqualTo(1);
    }

    @Test
    @DisplayName("여러 태그를 한 번에 저장하고 모두 ID가 생성됨")
    void saveTags_multipleTagsGetIds()
    {
        // given
        List<Tag> tags = List.of(
                Tag.from("Python"),
                Tag.from("JavaScript"),
                Tag.from("TypeScript"),
                Tag.from("Go"),
                Tag.from("Rust")
        );

        // when
        tagMapper.saveTags(tags);

        // then
        assertThat(tags).hasSize(5);
        tags.forEach(tag ->
        {
            assertThat(tag.getId()).isNotNull();
            assertThat(tag.getId()).isGreaterThan(0L);
            assertThat(tag.getName()).isNotNull();
        });

        // 각 태그의 ID가 서로 다른지 확인
        List<Long> ids = tags.stream().map(Tag::getId).toList();
        assertThat(ids).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("게시글에 여러 태그를 연결하고 모두 조회됨")
    void savePostTags_multipleTagsLinked()
    {
        // given
        Long postId = 1L;
        List<Tag> tags = List.of(
                Tag.builder().id(1L).name("Java").build(),
                Tag.builder().id(2L).name("Spring").build(),
                Tag.builder().id(3L).name("MySQL").build()
        );

        // when
        tagMapper.savePostTags(postId, tags);

        // then
        List<Long> tagIds = jdbcTemplate.queryForList(
                "SELECT tag_id FROM post_tags WHERE post_id = ?",
                Long.class,
                postId
        );

        assertThat(tagIds).hasSize(3);
        assertThat(tagIds).contains(1L, 2L, 3L);
    }

    @Test
    @DisplayName("같은 태그를 여러 게시글에 연결할 수 있음")
    void savePostTags_sameTagOnMultiplePosts()
    {
        // given
        Long postId1 = 1L;
        Long postId2 = 2L;
        Long javaTagId = 1L;

        List<Tag> tags = List.of(
                Tag.builder().id(javaTagId).name("Java").build()
        );

        // when
        tagMapper.savePostTags(postId1, tags);
        tagMapper.savePostTags(postId2, tags);

        // then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post_tags WHERE tag_id = ?",
                Integer.class,
                javaTagId
        );

        assertThat(count).isGreaterThanOrEqualTo(2);
    }
}
