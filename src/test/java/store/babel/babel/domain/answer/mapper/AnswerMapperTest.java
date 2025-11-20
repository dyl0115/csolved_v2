package store.babel.babel.domain.answer.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.answer.dto.Answer;
import store.babel.babel.domain.answer.dto.AnswerCreateCommand;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class AnswerMapperTest
{
    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp()
    {
        // FK 제약조건을 지키기 위해 미리 필요한 데이터를 저장.
        // AnswerMapper 테스트에 다른 Mapper에 의존하지 않도록 한다.

        // 사용자 데이터
        jdbcTemplate.execute(
                "INSERT INTO users (id, email, password, nickname, company, admin, created_at)\n" +
                        "VALUES (1, 'test1@example.com', 'password123', 'testUser1', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
                        "       (2, 'test2@example.com', 'password123', 'testUser2', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
                        "       (3, 'test3@example.com', 'password123', 'testUser3', 'testCompany', false, CURRENT_TIMESTAMP);");

        // 게시글 데이터 (답변이 달릴 게시글)
        jdbcTemplate.execute(
                "INSERT INTO posts (id, post_type, user_id, anonymous, title, content, answer_count, created_at)\n" +
                        "VALUES (1, 1, 1, false, '테스트 게시글1', '테스트 내용1', 0, CURRENT_TIMESTAMP),\n" +
                        "       (2, 1, 1, false, '테스트 게시글2', '테스트 내용2', 0, CURRENT_TIMESTAMP);");
    }

    @Test
    @DisplayName("답변을 저장하고 ID가 자동 생성되는지 확인")
    void saveAnswer()
    {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        String content = "테스트 답변";

        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(authorId)
                .anonymous(false)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        // when
        answerMapper.saveAnswer(command);

        // then
        assertThat(command.getId()).isNotNull();
        assertThat(command.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("특정 게시글의 답변 목록을 조회")
    void getAnswers()
    {
        // given
        Long postId = 1L;

        AnswerCreateCommand command1 = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(1L)
                .anonymous(false)
                .content("답변1")
                .createdAt(LocalDateTime.now())
                .build();

        AnswerCreateCommand command2 = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(2L)
                .anonymous(false)
                .content("답변2")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command1);
        answerMapper.saveAnswer(command2);

        // when
        List<Answer> answers = answerMapper.getAnswers(postId);

        // then
        assertThat(answers).isNotNull();
        assertThat(answers.size()).isGreaterThanOrEqualTo(2);
        assertThat(answers).anyMatch(a -> a.getContent().equals("답변1"));
        assertThat(answers).anyMatch(a -> a.getContent().equals("답변2"));
    }

    @Test
    @DisplayName("특정 답변을 ID로 조회")
    void getAnswer()
    {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        String content = "특정 답변";

        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(authorId)
                .anonymous(false)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command);

        // when
        Answer answer = answerMapper.getAnswer(command.getId());

        // then
        assertThat(answer).isNotNull();
        assertThat(answer.getId()).isEqualTo(command.getId());
        assertThat(answer.getContent()).isEqualTo(content);
        assertThat(answer.getAuthorId()).isEqualTo(authorId);
    }

    @Test
    @DisplayName("게시글의 답변 개수를 증가시키고 확인")
    void increaseAnswerCount()
    {
        // given
        Long postId = 1L;

        // 게시글의 초기 답변 개수 확인
        Integer beforeCount = jdbcTemplate.queryForObject(
                "SELECT answer_count FROM posts WHERE id = ?",
                Integer.class,
                postId
        );

        // when
        answerMapper.increaseAnswerCount(postId);

        // then
        Integer afterCount = jdbcTemplate.queryForObject(
                "SELECT answer_count FROM posts WHERE id = ?",
                Integer.class,
                postId
        );

        assertThat(afterCount).isEqualTo(beforeCount + 1);
    }

    @Test
    @DisplayName("게시글의 답변 개수를 감소시키고 확인")
    void decreaseAnswerCount()
    {
        // given
        Long postId = 1L;

        // 답변 개수를 먼저 증가시킴
        answerMapper.increaseAnswerCount(postId);

        Integer beforeCount = jdbcTemplate.queryForObject(
                "SELECT answer_count FROM posts WHERE id = ?",
                Integer.class,
                postId
        );

        // when
        answerMapper.decreaseAnswerCount(postId);

        // then
        Integer afterCount = jdbcTemplate.queryForObject(
                "SELECT answer_count FROM posts WHERE id = ?",
                Integer.class,
                postId
        );

        assertThat(afterCount).isEqualTo(beforeCount - 1);
    }

    @Test
    @DisplayName("댓글이 없는 답변은 false 반환")
    void existComments_whenNoComments()
    {
        // given
        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(1L)
                .authorId(1L)
                .anonymous(false)
                .content("댓글 없는 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command);

        // when
        boolean hasComments = answerMapper.existComments(command.getId());

        // then
        assertThat(hasComments).isFalse();
    }

    @Test
    @DisplayName("댓글이 있는 답변은 true 반환")
    void existComments_whenHasComments()
    {
        // given
        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(1L)
                .authorId(1L)
                .anonymous(false)
                .content("댓글 있는 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command);

        // 답변에 댓글 추가
        jdbcTemplate.execute(
                "INSERT INTO comments (post_id, answer_id, author_id, anonymous, content, created_at)\n" +
                        "VALUES (1, " + command.getId() + ", 2, false, '테스트 댓글', CURRENT_TIMESTAMP);"
        );

        // when
        boolean hasComments = answerMapper.existComments(command.getId());

        // then
        assertThat(hasComments).isTrue();
    }

    @Test
    @DisplayName("답변을 논리적으로 삭제하고 내용이 변경되는지 확인")
    void softDelete()
    {
        // given
        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(1L)
                .authorId(1L)
                .anonymous(false)
                .content("삭제될 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command);

        // when
        answerMapper.deleteAnswer(command.getId());

        // then
        Answer deletedAnswer = answerMapper.getAnswer(command.getId());
        assertThat(deletedAnswer.getContent()).isEqualTo("[삭제된 답변입니다.]");
    }

    @Test
    @DisplayName("답변을 물리적으로 삭제하고 조회되지 않는지 확인")
    void hardDelete()
    {
        // given
        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(1L)
                .authorId(1L)
                .anonymous(false)
                .content("완전히 삭제될 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command);
        Long answerId = command.getId();

        // when
        answerMapper.hardDelete(answerId);

        // then
        Answer deletedAnswer = answerMapper.getAnswer(answerId);
        assertThat(deletedAnswer).isNull();
    }

    @Test
    @DisplayName("익명 답변을 저장하고 조회")
    void saveAnonymousAnswer()
    {
        // given
        Long postId = 1L;

        AnswerCreateCommand command = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(1L)
                .anonymous(true)
                .content("익명 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command);

        // when
        List<Answer> answers = answerMapper.getAnswers(postId);

        // then
        assertThat(answers).isNotNull();
        Answer anonymousAnswer = answers.stream()
                .filter(a -> a.getContent().equals("익명 답변"))
                .findFirst()
                .orElse(null);

        assertThat(anonymousAnswer).isNotNull();
        assertThat(anonymousAnswer.isAnonymous()).isTrue();
    }

    @Test
    @DisplayName("여러 사용자가 같은 게시글에 답변을 달고 모두 조회")
    void multipleUsersAnswersOnSamePost()
    {
        // given
        Long postId = 1L;

        AnswerCreateCommand command1 = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(1L)
                .anonymous(false)
                .content("사용자1의 답변")
                .createdAt(LocalDateTime.now())
                .build();

        AnswerCreateCommand command2 = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(2L)
                .anonymous(false)
                .content("사용자2의 답변")
                .createdAt(LocalDateTime.now())
                .build();

        AnswerCreateCommand command3 = AnswerCreateCommand.builder()
                .postId(postId)
                .authorId(3L)
                .anonymous(false)
                .content("사용자3의 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command1);
        answerMapper.saveAnswer(command2);
        answerMapper.saveAnswer(command3);

        // when
        List<Answer> answers = answerMapper.getAnswers(postId);

        // then
        assertThat(answers).isNotNull();
        assertThat(answers.size()).isGreaterThanOrEqualTo(3);

        // 각 사용자의 답변이 모두 존재하는지 확인
        assertThat(answers).anyMatch(a -> a.getAuthorId().equals(1L) && a.getContent().equals("사용자1의 답변"));
        assertThat(answers).anyMatch(a -> a.getAuthorId().equals(2L) && a.getContent().equals("사용자2의 답변"));
        assertThat(answers).anyMatch(a -> a.getAuthorId().equals(3L) && a.getContent().equals("사용자3의 답변"));
    }

    @Test
    @DisplayName("다른 게시글에 답변을 달면 각각 별도로 조회됨")
    void answersOnDifferentPosts()
    {
        // given
        Long postId1 = 1L;
        Long postId2 = 2L;

        AnswerCreateCommand command1 = AnswerCreateCommand.builder()
                .postId(postId1)
                .authorId(1L)
                .anonymous(false)
                .content("게시글1의 답변")
                .createdAt(LocalDateTime.now())
                .build();

        AnswerCreateCommand command2 = AnswerCreateCommand.builder()
                .postId(postId2)
                .authorId(2L)
                .anonymous(false)
                .content("게시글2의 답변")
                .createdAt(LocalDateTime.now())
                .build();

        answerMapper.saveAnswer(command1);
        answerMapper.saveAnswer(command2);

        // when
        List<Answer> answersForPost1 = answerMapper.getAnswers(postId1);
        List<Answer> answersForPost2 = answerMapper.getAnswers(postId2);

        // then
        assertThat(answersForPost1).anyMatch(a -> a.getContent().equals("게시글1의 답변"));
        assertThat(answersForPost1).noneMatch(a -> a.getContent().equals("게시글2의 답변"));

        assertThat(answersForPost2).anyMatch(a -> a.getContent().equals("게시글2의 답변"));
        assertThat(answersForPost2).noneMatch(a -> a.getContent().equals("게시글1의 답변"));
    }
}
