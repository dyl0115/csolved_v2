CREATE TABLE notice
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    author_id   BIGINT NOT NULL REFERENCES User (id),

    -- 공지사항 특화 컬럼들
    is_pinned   BOOLEAN   DEFAULT FALSE, -- 상단 고정

    -- 기본 관리
    views  INTEGER   DEFAULT 0,     -- 조회수
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP DEFAULT NULL
);