CREATE TABLE `attach` (
  `file_id`           int(10) unsigned NOT NULL AUTO_INCREMENT,
  `belong_type`       varchar(20) NOT NULL DEFAULT '0',
  `belong_id`         int(10) unsigned NOT NULL DEFAULT '0',
  `file_type`         varchar(50) NOT NULL DEFAULT '''''',
  `file_length`       int(10) unsigned NOT NULL DEFAULT '0',
  `download_times`    int(10) unsigned NOT NULL DEFAULT '0',
  `create_user_id`    int(11) unsigned NOT NULL DEFAULT '0',
  `create_time`       timestamp NULL DEFAULT NULL,
  `status`            tinyint(3) unsigned NOT NULL DEFAULT '0',
  `remarks`           varchar(100) NOT NULL DEFAULT '''''',
  `read_level`        int(10) NOT NULL DEFAULT '0',
  `client_file_name`  varchar(200) NOT NULL DEFAULT '''''',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6405 DEFAULT CHARSET=utf8;
