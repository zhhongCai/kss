
CREATE TABLE `base_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `password` VARCHAR(128) COLLATE UTF8_BIN DEFAULT NULL,
    `code` VARCHAR(32) COLLATE UTF8_BIN DEFAULT NULL,
    `phone` VARCHAR(16) COLLATE UTF8_BIN DEFAULT NULL,
    `department` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `create_time` DATETIME DEFAULT NULL,
    `create_user` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `modify_time` DATETIME DEFAULT NULL,
    `modify_user` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `account_non_expired` INT(1) COLLATE UTF8_BIN DEFAULT NULL,
    `account_non_locked` INT(1) COLLATE UTF8_BIN DEFAULT NULL,
    `credentials_non_expired` INT(1) COLLATE UTF8_BIN DEFAULT NULL,
    `enabled` INT(1) COLLATE UTF8_BIN DEFAULT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN;


CREATE TABLE `base_user_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id`  BIGINT(20) NOT NULL,
    `role` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `create_time` DATETIME DEFAULT NULL,
    `create_user` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `modify_time` DATETIME DEFAULT NULL,
    `modify_user` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN;


CREATE TABLE `login_history` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) DEFAULT NULL,
    `user_name` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `login_id` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `request_ip` VARCHAR(16) COLLATE UTF8_BIN DEFAULT NULL,
    `login_time` DATETIME DEFAULT NULL,
    `token` VARCHAR(128) COLLATE UTF8_BIN DEFAULT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN;


CREATE TABLE `db_data_source` (
	`id` BIGINT (20) NOT NULL AUTO_INCREMENT,
	`data_source_name` VARCHAR (60) NOT NULL,
	`url` VARCHAR (300) NOT NULL,
	`user_name` VARCHAR (30) NOT NULL,
	`driver_class_name` VARCHAR (60) NOT NULL,
	`password` VARCHAR (128) NOT NULL,
	`create_time` DATETIME DEFAULT NULL,
	`create_user` VARCHAR (64) COLLATE UTF8_BIN DEFAULT NULL,
	`modify_time` DATETIME DEFAULT NULL,
	`modify_user` VARCHAR (64) COLLATE UTF8_BIN DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8 COLLATE = UTF8_BIN;


CREATE TABLE `kss_project` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(128) COLLATE UTF8_BIN DEFAULT NULL,
    `code` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `db_name` VARCHAR(16) COLLATE UTF8_BIN DEFAULT NULL,
    `description` VARCHAR(256) COLLATE UTF8_BIN DEFAULT NULL,
    `table_schema` TEXT COLLATE UTF8_BIN DEFAULT NULL,

    `create_time` DATETIME DEFAULT NULL,
    `create_user` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    `modify_time` DATETIME DEFAULT NULL,
    `modify_user` VARCHAR(64) COLLATE UTF8_BIN DEFAULT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN;

CREATE TABLE `user_rel_project` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `project_id`  BIGINT NOT NULL,

    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN;


CREATE TABLE `project_rel_data_source` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_id`  BIGINT NOT NULL,
    `data_source_id` BIGINT NOT NULL,

    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8 COLLATE = UTF8_BIN;



-- admin/admin123
INSERT INTO `manage-web`.`base_user` (`id`, `username`, `password`, `code`, `phone`, `department`, `create_time`, `create_user`, `modify_time`, `modify_user`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`)
VALUES (1, 'admin', '$2a$10$s.aeELk9MOBXG5Ct8AdYdOVUBMyWeP3iKoNzVUs6p1Sb6Krx8.HE2', 'admin', '18650345343', 'test', now(), 'admin', now(), NULL, 1, 1, 1, 1);

INSERT INTO `manage-web`.`base_user_role` (`id`, `user_id`, `role`, `create_time`, `create_user`, `modify_time`, `modify_user`)
VALUES (1, 1, 'ROLE_ADMIN', now(), 'admin', now(), 'admin');