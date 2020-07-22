SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`
(
    `blog_id`         int(11) NOT NULL AUTO_INCREMENT,
    `type_id`         int(11)      DEFAULT NULL,
    `user_id`         int(11)      DEFAULT NULL,
    `title`           varchar(255) DEFAULT NULL,
    `content`         longtext,
    `first_picture`   varchar(255) DEFAULT NULL,
    `flag`            varchar(255) DEFAULT NULL,
    `views`           int(11)      DEFAULT NULL,
    `appreciation`    tinyint(1)   DEFAULT '0',
    `share_statement` tinyint(1)   DEFAULT '0',
    `comment`         tinyint(1)   DEFAULT '0',
    `publish`         tinyint(1)   DEFAULT '0',
    `recommend`       tinyint(1)   DEFAULT '0',
    `create_time`     datetime     DEFAULT NULL,
    `update_time`     datetime     DEFAULT NULL,
    `description`     longtext,
    PRIMARY KEY (`blog_id`),
    KEY `FK_Reference_1` (`type_id`),
    KEY `FK_Reference_2` (`user_id`),
    CONSTRAINT `FK_Reference_1` FOREIGN KEY (`type_id`) REFERENCES `type` (`type_id`),
    CONSTRAINT `FK_Reference_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`
(
    `blog_tag_id` int(11) NOT NULL AUTO_INCREMENT,
    `blog_id`     int(11) DEFAULT NULL,
    `tag_id`      int(11) DEFAULT NULL,
    PRIMARY KEY (`blog_tag_id`),
    KEY `FK_Reference_4` (`blog_id`),
    KEY `FK_Reference_5` (`tag_id`),
    CONSTRAINT `FK_Reference_4` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`),
    CONSTRAINT `FK_Reference_5` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `comment_id`        int(11) NOT NULL AUTO_INCREMENT,
    `blog_id`           int(11)      DEFAULT NULL,
    `nickname`         varchar(255) DEFAULT NULL,
    `email`             varchar(255) DEFAULT NULL,
    `content`           varchar(255) DEFAULT NULL,
    `avatar`            varchar(255) DEFAULT NULL,
    `create_time`       datetime     DEFAULT NULL,
    `parent_comment_id` int(11)      DEFAULT NULL,
    PRIMARY KEY (`comment_id`),
    KEY `FK_Reference_3` (`blog_id`),
    CONSTRAINT `FK_Reference_3` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `tag_id` int(11) NOT NULL AUTO_INCREMENT,
    `name`   varchar(255) DEFAULT NULL,
    PRIMARY KEY (`tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`
(
    `type_id` int(11) NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`type_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`     int(11) NOT NULL AUTO_INCREMENT,
    `nickname`   varchar(255) DEFAULT NULL,
    `username`    varchar(255) DEFAULT NULL,
    `password`    varchar(255) DEFAULT NULL,
    `email`       varchar(255) DEFAULT NULL,
    `avatar`      varchar(255) DEFAULT NULL,
    `type`        int(11)      DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES ('1', '小方方', 'hustffx', 'e10adc3949ba59abbe56e057f20f883e', 'fangfengxin98@163.com',
        'https://picsum.photos/id/883/200/200',
        null, null, null);