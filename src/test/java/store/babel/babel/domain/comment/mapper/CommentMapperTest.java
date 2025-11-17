package store.babel.babel.domain.comment.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.comment.dto.Comment;
import store.babel.babel.domain.comment.dto.CommentCreateCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class CommentMapperTest
{
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp()
    {
        // FK 제약조건을 지키기 위해 미리 필요한 데이터를 저장.
        // CommentMapper 테스트에 다른 Mapper에 의존하지 않도록 한다.

        // 사용자 데이터
        jdbcTemplate.execute(
                "INSERT INTO users (id, email, password, nickname, company, admin, created_at)\n" +
                        "VALUES (1, 'test1@example.com', 'password123', 'testUser1', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
                        "       (2, 'test2@example.com', 'password123', 'testUser2', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
                        "       (3, 'test3@example.com', 'password123', 'testUser3', 'testCompany', false, CURRENT_TIMESTAMP);");

        // 게시글 데이터 (댓글이 달릴 게시글)
        jdbcTemplate.execute(
                "INSERT INTO posts (id, post_type, user_id, anonymous, title, content, created_at)\n" +
                        "VALUES (1, 1, 1, false, '테스트 게시글', '테스트 내용', CURRENT_TIMESTAMP);");

        // 답변 데이터 (댓글이 달릴 답변)
        jdbcTemplate.execute(
                "INSERT INTO answers (id, post_id, author_id, anonymous, content, created_at)\n" +
                        "VALUES (1, 1, 2, false, '테스트 답변', CURRENT_TIMESTAMP),\n" +
                        "       (2, 1, 2, false, '테스트 답변2', CURRENT_TIMESTAMP);");
    }

    @Test
    @DisplayName("댓글을 저장하고 ID가 자동 생성되는지 확인")
    void saveComment()
    {
        // given
        Long postId = 1L;
        Long answerId = 1L;
        Long authorId = 1L;
        String content = "테스트 댓글";

        CommentCreateCommand command = CommentCreateCommand.builder()
                .postId(postId)
                .answerId(answerId)
                .authorId(authorId)
                .anonymous(false)
                .content(content)
                .build();

        // when
        commentMapper.save(command);

        // then
        assertThat(command.getId()).isNotNull();
        assertThat(command.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("특정 답변의 댓글 목록을 조회")
    void getComments()
    {
        // given
        Long answerId = 1L;

        CommentCreateCommand command1 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId)
                .authorId(1L)
                .anonymous(false)
                .content("댓글1")
                .build();

        CommentCreateCommand command2 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId)
                .authorId(2L)
                .anonymous(false)
                .content("댓글2")
                .build();

        commentMapper.save(command1);
        commentMapper.save(command2);

        // when
        List<Comment> comments = commentMapper.getComments(List.of(answerId));

        // then
        assertThat(comments).isNotNull();
        assertThat(comments.size()).isGreaterThanOrEqualTo(2);
        assertThat(comments).anyMatch(c -> c.getContent().equals("댓글1"));
        assertThat(comments).anyMatch(c -> c.getContent().equals("댓글2"));
    }

    @Test
    @DisplayName("여러 답변의 댓글을 한 번에 조회")
    void getCommentsForMultipleAnswers()
    {
        // given
        Long answerId1 = 1L;
        Long answerId2 = 2L;

        CommentCreateCommand command1 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId1)
                .authorId(1L)
                .anonymous(false)
                .content("답변1의 댓글")
                .build();

        CommentCreateCommand command2 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId2)
                .authorId(2L)
                .anonymous(false)
                .content("답변2의 댓글")
                .build();

        commentMapper.save(command1);
        commentMapper.save(command2);

        // when
        List<Comment> comments = commentMapper.getComments(List.of(answerId1, answerId2));

        // then
        assertThat(comments).isNotNull();
        assertThat(comments.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("댓글 작성자 ID를 정확히 조회")
    void getAuthorId()
    {
        // given
        Long expectedAuthorId = 1L;

        CommentCreateCommand command = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(1L)
                .authorId(expectedAuthorId)
                .anonymous(false)
                .content("테스트 댓글")
                .build();

        commentMapper.save(command);

        // when
        Long authorId = commentMapper.getAuthorId(command.getId());

        // then
        assertThat(authorId).isEqualTo(expectedAuthorId);
    }

    @Test
    @DisplayName("댓글을 삭제하고 조회되지 않는지 확인")
    void deleteComment()
    {
        // given
        CommentCreateCommand command = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(1L)
                .authorId(1L)
                .anonymous(false)
                .content("삭제될 댓글")
                .build();

        commentMapper.save(command);
        Long commentId = command.getId();

        // when
        commentMapper.delete(commentId);

        // then
        Long authorId = commentMapper.getAuthorId(commentId);
        assertThat(authorId).isNull();
    }

    @Test
    @DisplayName("익명 댓글을 저장하고 조회")
    void saveAnonymousComment()
    {
        // given
        Long answerId = 1L;

        CommentCreateCommand command = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId)
                .authorId(1L)
                .anonymous(true)
                .content("익명 댓글")
                .build();

        commentMapper.save(command);

        // when
        List<Comment> comments = commentMapper.getComments(List.of(answerId));

        // then
        assertThat(comments).isNotNull();
        Comment anonymousComment = comments.stream()
                .filter(c -> c.getContent().equals("익명 댓글"))
                .findFirst()
                .orElse(null);

        assertThat(anonymousComment).isNotNull();
        assertThat(anonymousComment.isAnonymous()).isTrue();
    }

    @Test
    @DisplayName("빈 답변 ID 리스트로 조회 시 빈 리스트 반환")
    void getCommentsWithEmptyList()
    {
        // given
        List<Long> emptyAnswerIds = List.of();

        // when
        List<Comment> comments = commentMapper.getComments(emptyAnswerIds);

        // then
        assertThat(comments).isNotNull();
        assertThat(comments).isEmpty();
    }

    @Test
    @DisplayName("여러 사용자가 같은 답변에 댓글을 달고 모두 조회")
    void multipleUsersCommentsOnSameAnswer()
    {
        // given
        Long answerId = 1L;

        CommentCreateCommand command1 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId)
                .authorId(1L)
                .anonymous(false)
                .content("사용자1의 댓글")
                .build();

        CommentCreateCommand command2 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId)
                .authorId(2L)
                .anonymous(false)
                .content("사용자2의 댓글")
                .build();

        CommentCreateCommand command3 = CommentCreateCommand.builder()
                .postId(1L)
                .answerId(answerId)
                .authorId(3L)
                .anonymous(false)
                .content("사용자3의 댓글")
                .build();

        commentMapper.save(command1);
        commentMapper.save(command2);
        commentMapper.save(command3);

        // when
        List<Comment> comments = commentMapper.getComments(List.of(answerId));

        // then
        assertThat(comments).isNotNull();
        assertThat(comments.size()).isGreaterThanOrEqualTo(3);

        // 각 사용자의 댓글이 모두 존재하는지 확인
        assertThat(comments).anyMatch(c -> c.getAuthorId().equals(1L) && c.getContent().equals("사용자1의 댓글"));
        assertThat(comments).anyMatch(c -> c.getAuthorId().equals(2L) && c.getContent().equals("사용자2의 댓글"));
        assertThat(comments).anyMatch(c -> c.getAuthorId().equals(3L) && c.getContent().equals("사용자3의 댓글"));
    }
}
