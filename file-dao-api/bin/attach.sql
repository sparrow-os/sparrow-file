DROP TABLE IF EXISTS `attach`;
CREATE TABLE `attach` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `file_id` varchar(64)  ,
 `client_file_name` varchar(256) DEFAULT ''  NOT NULL,
 `download_times` int(11) UNSIGNED DEFAULT 0  NOT NULL,
 `content_length` int(11) UNSIGNED DEFAULT 0  NOT NULL,
 `content_type` varchar(256) DEFAULT ''  NOT NULL,
 `read_level` int(11) UNSIGNED DEFAULT 0  ,
 `create_time` bigint(20) DEFAULT 0  NOT NULL,
 `create_user_id` int(11) UNSIGNED DEFAULT 0  NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='attach';
