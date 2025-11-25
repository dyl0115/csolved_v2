package store.babel.babel.domain.notice.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import store.babel.babel.domain.notice.dto.*;
import store.babel.babel.global.utils.page.Pagination;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static store.babel.babel.domain.post.dto.PostType.NOTICE;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class NoticeMapperTest
{
//    @Autowired
//    private NoticeMapper noticeMapper;
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @BeforeAll
//    void setUp()
//    {
//        // FK 제약조건을 지키기 위해 미리 필요한 데이터를 저장.
//        // NoticeMapper 테스트에 다른 Mapper에 의존하지 않도록 한다.
//
//        // 사용자 데이터
//        jdbcTemplate.execute(
//                "INSERT INTO users (id, email, password, nickname, company, admin, created_at)\n" +
//                        "VALUES (1, 'test1@example.com', 'password123', 'testUser1', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
//                        "       (2, 'test2@example.com', 'password123', 'testUser2', 'testCompany', false, CURRENT_TIMESTAMP),\n" +
//                        "       (3, 'admin@example.com', 'password123', 'adminUser', 'testCompany', true, CURRENT_TIMESTAMP);");
//    }
//
//    @Test
//    @DisplayName("공지사항을 저장하고 해당 공지사항이 잘 조회되는지 확인")
//    void saveNotice()
//    {
//        // given
//        Long authorId = 3L;
//        String title = "테스트 공지사항";
//        String content = "테스트 내용";
//
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title(title)
//                .content(content)
//                .authorId(authorId)
//                .anonymous(false)
//                .build();
//
//        // when
//        noticeMapper.saveNotice(command);
//
//        // then
//        Notice notice = noticeMapper.getNotice(command.getId());
//
//        assertThat(command.getId()).isNotNull();
//        assertThat(command.getId()).isGreaterThan(0L);
//
//        assertThat(notice.getTitle()).isEqualTo(title);
//        assertThat(notice.getContent()).isEqualTo(content);
//        assertThat(notice.getAuthorId()).isEqualTo(authorId);
//    }
//
//    @Test
//    @DisplayName("공지사항을 수정하고 변경사항이 반영되는지 확인")
//    void updateNotice()
//    {
//        // given
//        NoticeCreateCommand createCommand = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(3L)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(createCommand);
//
//        // when
//        NoticeUpdateCommand updateCommand = NoticeUpdateCommand.builder()
//                .noticeId(createCommand.getId())
//                .title("수정된 제목")
//                .content("수정된 내용")
//                .authorId(3L)
//                .anonymous(true)
//                .build();
//
//        noticeMapper.updateNotice(updateCommand);
//
//        // then
//        Notice updatedNotice = noticeMapper.getNotice(createCommand.getId());
//        assertThat(updatedNotice.getTitle()).isEqualTo("수정된 제목");
//        assertThat(updatedNotice.getContent()).isEqualTo("수정된 내용");
//        assertThat(updatedNotice.isAnonymous()).isTrue();
//    }
//
//    @Test
//    @DisplayName("공지사항을 논리적으로 삭제하고 조회되지 않는지 확인")
//    void deleteNotice()
//    {
//        // given
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(3L)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//
//        // when
//        noticeMapper.deleteNotice(command.getId());
//
//        // then
//        NoticeSearchQuery query = NoticeSearchQuery.builder()
//                .postType(NOTICE.getValue())
//                .searchType("TITLE")
//                .searchKeyword("")
//                .build();
//
//        Long count = noticeMapper.countNotices(query);
//        assertThat(count).isEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("검색 조건에 맞는 공지사항 목록을 조회")
//    void getNoticeCards()
//    {
//        Long adminId = 3L;
//
//        // given
//        NoticeCreateCommand command1 = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("공지1")
//                .content("공지1 내용")
//                .authorId(adminId)
//                .anonymous(false)
//                .build();
//
//        NoticeCreateCommand command2 = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("공지2")
//                .content("공지2 내용")
//                .authorId(adminId)
//                .anonymous(false)
//                .build();
//
//        NoticeCreateCommand command3 = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("공지3")
//                .content("공지3 내용")
//                .authorId(adminId)
//                .anonymous(false)
//                .build();
//
//        NoticeSearchQuery query = NoticeSearchQuery.builder()
//                .postType(NOTICE.getValue())
//                .searchType("TITLE")
//                .searchKeyword("")
//                .build();
//
//        noticeMapper.saveNotice(command1);
//        noticeMapper.saveNotice(command2);
//        noticeMapper.saveNotice(command3);
//
//        // when
//        Long noticeCount = noticeMapper.countNotices(query);
//        Pagination pagination = Pagination.from(1L, noticeCount);
//        List<NoticeCard> noticeCards = noticeMapper.getNoticeCards(query, pagination);
//
//        // then
//        assertThat(noticeCards.size()).isEqualTo(3);
//        assertThat(noticeCards.get(0).getAuthorId()).isEqualTo(adminId);
//        assertThat(noticeCards.get(1).getAuthorId()).isEqualTo(adminId);
//        assertThat(noticeCards.get(2).getAuthorId()).isEqualTo(adminId);
//    }
//
//    @Test
//    @DisplayName("제목으로 공지사항을 검색하고 결과를 확인")
//    void getNoticeCardsByTitle()
//    {
//        // given
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("중요공지")
//                .content("중요한 내용")
//                .authorId(3L)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//
//        NoticeSearchQuery query = NoticeSearchQuery.builder()
//                .postType(NOTICE.getValue())
//                .searchType("TITLE")
//                .searchKeyword("중요")
//                .build();
//        Pagination pagination = Pagination.from(1L, 10L);
//
//        // when
//        List<NoticeCard> noticeCards = noticeMapper.getNoticeCards(query, pagination);
//
//        // then
//        assertThat(noticeCards).isNotNull();
//        noticeCards.forEach(card ->
//                assertThat(card.getTitle()).contains("중요")
//        );
//    }
//
//    @Test
//    @DisplayName("검색 조건에 맞는 공지사항 개수를 정확히 계산")
//    void countNotices()
//    {
//        // given
//        NoticeSearchQuery query = NoticeSearchQuery.builder()
//                .postType(NOTICE.getValue())
//                .searchType("TITLE")
//                .searchKeyword("")
//                .build();
//
//        // when
//        Long count = noticeMapper.countNotices(query);
//
//        // then
//        assertThat(count).isNotNull();
//        assertThat(count).isGreaterThanOrEqualTo(0L);
//    }
//
//    @Test
//    @DisplayName("특정 검색어로 공지사항 개수를 정확히 계산")
//    void countNoticesByKeyword()
//    {
//        // given
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("검색테스트")
//                .content("검색용 내용")
//                .authorId(3L)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//
//        NoticeSearchQuery query = NoticeSearchQuery.builder()
//                .postType(NOTICE.getValue())
//                .searchType("TITLE")
//                .searchKeyword("검색테스트")
//                .build();
//
//        // when
//        Long count = noticeMapper.countNotices(query);
//
//        // then
//        assertThat(count).isNotNull();
//        assertThat(count).isGreaterThanOrEqualTo(1L);
//    }
//
//    @Test
//    @DisplayName("사용자가 좋아요를 누르지 않았을 때 false 반환")
//    void hasUserLiked_whenNotLiked()
//    {
//        // given
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(3L)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//        Long noticeId = command.getId();
//        Long userId = 1L;
//
//        // when
//        boolean hasLiked = noticeMapper.hasUserLiked(noticeId, userId);
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
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(3L)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//
//        // when
//        Long noticeId = command.getId();
//        noticeMapper.addUserLike(noticeId, userId);
//
//        // then
//        boolean hasLiked = noticeMapper.hasUserLiked(noticeId, userId);
//        assertThat(hasLiked).isTrue();
//    }
//
//    @Test
//    @DisplayName("공지사항의 좋아요 수를 증가시키고 확인")
//    void increaseLikes()
//    {
//        // given
//        Long authorId = 3L;
//
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(authorId)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//
//        Long noticeId = command.getId();
//
//        Notice beforeNotice = noticeMapper.getNotice(noticeId);
//        Long beforeLikes = beforeNotice.getLikes();
//
//        // when
//        noticeMapper.increaseLikes(noticeId);
//
//        // then
//        Notice afterNotice = noticeMapper.getNotice(noticeId);
//        assertThat(afterNotice.getLikes()).isEqualTo(beforeLikes + 1);
//    }
//
//    @Test
//    @DisplayName("공지사항의 조회수를 증가시키고 확인")
//    void increaseView()
//    {
//        // given
//        Long authorId = 3L;
//
//        NoticeCreateCommand command = NoticeCreateCommand.builder()
//                .postType(NOTICE.getValue())
//                .title("제목")
//                .content("내용")
//                .authorId(authorId)
//                .anonymous(false)
//                .build();
//
//        noticeMapper.saveNotice(command);
//        Long noticeId = command.getId();
//
//        Notice beforeNotice = noticeMapper.getNotice(noticeId);
//        Long beforeViews = beforeNotice.getViews();
//
//        // when
//        noticeMapper.increaseView(noticeId);
//
//        // then
//        Notice afterNotice = noticeMapper.getNotice(noticeId);
//        assertThat(afterNotice.getViews()).isEqualTo(beforeViews + 1);
//    }
}
