-- MySQL dump 10.13  Distrib 8.0.39, for Win64 (x86_64)
-- Host: localhost    Database: csolved

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `users`
--

CREATE TABLE `users`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT,
    `profile_image` varchar(2083)         DEFAULT NULL,
    `email`         varchar(100) NOT NULL,
    `password`      varchar(200) NOT NULL,
    `nickname`      varchar(50)  NOT NULL,
    `company`       varchar(100)          DEFAULT NULL,
    `admin`         tinyint(1)            DEFAULT NULL,
    `created_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`   timestamp    NULL     DEFAULT NULL,
    `deleted_at`    timestamp    NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`),
    UNIQUE KEY `uk_nickname` (`nickname`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `category`
--

CREATE TABLE `category`
(
    `id`          bigint    NOT NULL AUTO_INCREMENT,
    `post_type`   tinyint        DEFAULT NULL,
    `name`        varchar(100)   DEFAULT NULL,
    `created_at`  timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at` timestamp NULL DEFAULT NULL,
    `deleted_at`  timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `tags`
--

CREATE TABLE `tags`
(
    `id`          bigint    NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
    `created_at`  timestamp NOT NULL                                     DEFAULT CURRENT_TIMESTAMP,
    `modified_at` timestamp NULL                                         DEFAULT NULL,
    `deleted_at`  timestamp NULL                                         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `tag_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `posts`
--

CREATE TABLE `posts`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `post_type`    tinyint      NOT NULL,
    `user_id`      bigint       NOT NULL,
    `anonymous`    tinyint(1)            DEFAULT '0',
    `title`        varchar(200) NOT NULL,
    `content`      text         NOT NULL,
    `category_id`  bigint                DEFAULT NULL,
    `views`        int                   DEFAULT '0',
    `likes`        bigint                DEFAULT '0',
    `answer_count` bigint                DEFAULT '0',
    `created_at`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`  timestamp    NULL     DEFAULT NULL,
    `deleted_at`   timestamp    NULL     DEFAULT NULL,
    `deleted_type` varchar(20)           DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    KEY `fk_category` (`category_id`),
    CONSTRAINT `fk_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
    CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `answers`
--

CREATE TABLE `answers`
(
    `id`           bigint    NOT NULL AUTO_INCREMENT,
    `post_id`      bigint    NOT NULL,
    `author_id`    bigint    NOT NULL,
    `anonymous`    tinyint(1)         DEFAULT NULL,
    `content`      text      NOT NULL,
    `created_at`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`  timestamp NULL     DEFAULT NULL,
    `deleted_at`   timestamp NULL     DEFAULT NULL,
    `deleted_type` varchar(20)        DEFAULT NULL,
    `likes`        bigint             DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `question_id` (`post_id`),
    KEY `user_id` (`author_id`),
    CONSTRAINT `answers_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `comments`
--

CREATE TABLE `comments`
(
    `id`           bigint    NOT NULL AUTO_INCREMENT,
    `post_id`      bigint             DEFAULT NULL,
    `answer_id`    bigint    NOT NULL,
    `author_id`    bigint    NOT NULL,
    `anonymous`    tinyint(1)         DEFAULT '0',
    `content`      text      NOT NULL,
    `created_at`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`  timestamp NULL     DEFAULT NULL,
    `deleted_at`   timestamp NULL     DEFAULT NULL,
    `deleted_type` varchar(20)        DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `answer_id` (`answer_id`),
    KEY `user_id` (`author_id`),
    KEY `fk_comments_post` (`post_id`),
    CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`),
    CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_comments_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `answer_likes`
--

CREATE TABLE `answer_likes`
(
    `id`         bigint    NOT NULL AUTO_INCREMENT,
    `answer_id`  bigint    NOT NULL,
    `user_id`    bigint    NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_answer_user` (`answer_id`, `user_id`),
    KEY `fk_answer_likes_user` (`user_id`),
    CONSTRAINT `fk_answer_likes_answer` FOREIGN KEY (`answer_id`) REFERENCES `answers` (`id`),
    CONSTRAINT `fk_answer_likes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `bookmarks`
--

CREATE TABLE `bookmarks`
(
    `id`         bigint    NOT NULL AUTO_INCREMENT,
    `post_id`    bigint    NOT NULL,
    `user_id`    bigint    NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_bookmarks_post_user` (`post_id`, `user_id`),
    KEY `idx_user_post` (`user_id`, `post_id`),
    CONSTRAINT `bookmarks_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
    CONSTRAINT `bookmarks_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Table structure for table `notice`
--

CREATE TABLE `notice`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `title`        varchar(255) NOT NULL,
    `content`      text         NOT NULL,
    `author_id`    bigint       NOT NULL,
    `is_pinned`    tinyint(1)            DEFAULT '0',
    `views`        int                   DEFAULT '0',
    `created_at`   timestamp    NULL     DEFAULT CURRENT_TIMESTAMP,
    `modified_at`  timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`   timestamp    NULL     DEFAULT NULL,
    `answer_count` bigint       NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `post_likes`
--

CREATE TABLE `post_likes`
(
    `id`         bigint    NOT NULL AUTO_INCREMENT,
    `post_id`    bigint    NOT NULL,
    `user_id`    bigint    NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_question_likes` (`post_id`, `user_id`),
    KEY `fk_question_likes_user` (`user_id`),
    CONSTRAINT `fk_question_likes_question` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_question_likes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `post_tags`
--

CREATE TABLE `post_tags`
(
    `post_id`    bigint    NOT NULL,
    `tag_id`     bigint    NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`post_id`, `tag_id`),
    KEY `question_tags_ibfk_2` (`tag_id`),
    CONSTRAINT `post_tags_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
    CONSTRAINT `post_tags_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `reports`
--

CREATE TABLE `reports`
(
    `id`            bigint      NOT NULL AUTO_INCREMENT,
    `reporter_id`   bigint      NOT NULL,
    `target_type`   varchar(20) NOT NULL,
    `target_id`     bigint      NOT NULL,
    `reason`        text        NOT NULL,
    `status`        varchar(20) NOT NULL DEFAULT 'PENDING',
    `created_at`    timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_at`   timestamp   NULL     DEFAULT NULL,
    `deleted_at`    timestamp   NULL     DEFAULT NULL,
    `detail_reason` text,
    `processed_by`  bigint               DEFAULT NULL,
    `processed_at`  datetime             DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `reporter_id` (`reporter_id`),
    CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`reporter_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
