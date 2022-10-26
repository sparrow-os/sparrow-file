DROP TABLE IF EXISTS `attach_ref`;
CREATE TABLE `attach_ref` (
 `id` int(11) UNSIGNED AUTO_INCREMENT NOT NULL ,
 `file_id` int(11) UNSIGNED DEFAULT 0 COMMENT 'FILE ID'  NOT NULL,
 `belong_type` varchar(64) DEFAULT '' COMMENT '所属业务'  NOT NULL,
 `business_id` int(10) UNSIGNED     COMMENT 'business id'  NOT NULL,
 `status` tinyint(1) DEFAULT 0 COMMENT 'status'  NOT NULL,
 `remarks` varchar(512) DEFAULT '' COMMENT '备注'  NOT NULL,
 `create_time` bigint(20) DEFAULT 0  NOT NULL,
 `update_time` bigint(20) DEFAULT 0  NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='attach_ref';
