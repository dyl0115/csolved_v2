-- Users table
CREATE TABLE users
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    profile_image VARCHAR(2083)         DEFAULT NULL,
    email         VARCHAR(100) NOT NULL,
    password      VARCHAR(200) NOT NULL,
    nickname      VARCHAR(50)  NOT NULL,
    company       VARCHAR(100)          DEFAULT NULL,
    admin         BOOLEAN               DEFAULT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at   TIMESTAMP    NULL     DEFAULT NULL,
    deleted_at    TIMESTAMP    NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (nickname)
);

-- Category table
CREATE TABLE category
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    post_type   TINYINT        DEFAULT NULL,
    name        VARCHAR(100)   DEFAULT NULL,
    created_at  TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NULL DEFAULT NULL,
    deleted_at  TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

-- Posts table
CREATE TABLE posts
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    post_type    TINYINT      NOT NULL,
    user_id      BIGINT       NOT NULL,
    anonymous    BOOLEAN               DEFAULT FALSE,
    title        VARCHAR(200) NOT NULL,
    content      TEXT         NOT NULL,
    category_id  BIGINT                DEFAULT NULL,
    views        INT                   DEFAULT 0,
    likes        BIGINT                DEFAULT 0,
    answer_count BIGINT                DEFAULT 0,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  TIMESTAMP    NULL     DEFAULT NULL,
    deleted_at   TIMESTAMP    NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

-- Answers table
CREATE TABLE answers
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    post_id     BIGINT    NOT NULL,
    author_id   BIGINT    NOT NULL,
    anonymous   BOOLEAN            DEFAULT NULL,
    content     TEXT      NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NULL     DEFAULT NULL,
    deleted_at  TIMESTAMP NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

-- Comments table
CREATE TABLE comments
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    post_id     BIGINT             DEFAULT NULL,
    answer_id   BIGINT    NOT NULL,
    author_id   BIGINT    NOT NULL,
    anonymous   BOOLEAN            DEFAULT FALSE,
    content     TEXT      NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NULL     DEFAULT NULL,
    deleted_at  TIMESTAMP NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (answer_id) REFERENCES answers (id),
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id)
);

-- Tags table
CREATE TABLE tags
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50)        DEFAULT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NULL     DEFAULT NULL,
    deleted_at  TIMESTAMP NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

-- Post_tags table
CREATE TABLE post_tags
(
    post_id    BIGINT    NOT NULL,
    tag_id     BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

-- Bookmarks table
CREATE TABLE bookmarks
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    post_id     BIGINT    NOT NULL,
    user_id     BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NULL     DEFAULT NULL,
    deleted_at  TIMESTAMP NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_user_post ON bookmarks (user_id, post_id);
CREATE INDEX idx_deleted_at ON bookmarks (deleted_at);

-- Post_likes table
CREATE TABLE post_likes
(
    id         BIGINT    NOT NULL AUTO_INCREMENT,
    post_id    BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Notice table
CREATE TABLE notice
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    author_id   BIGINT       NOT NULL,
    is_pinned   BOOLEAN           DEFAULT FALSE,
    views       INT               DEFAULT 0,
    created_at  TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP    NULL DEFAULT NULL,
    deleted_at  TIMESTAMP    NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

-- Reports table
CREATE TABLE reports
(
    id          BIGINT      NOT NULL,
    reporter_id BIGINT      NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id   BIGINT      NOT NULL,
    reason      TEXT        NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP   NULL     DEFAULT NULL,
    deleted_at  TIMESTAMP   NULL     DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (reporter_id) REFERENCES users (id)
);