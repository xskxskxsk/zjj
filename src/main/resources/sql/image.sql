DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`
(
  `id`          int      NOT NULL AUTO_INCREMENT,
  `user_id`     int       NOT NULL,
  `md5`   varchar(128)       NOT NULL,
  `name` varchar(128)       DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
