DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`
(
  `id`          INT       NOT NULL AUTO_INCREMENT,
  `user_id`     INT       NOT NULL,
  `MD5`   varchar(128)       NOT NULL,
  `name` varchar(128)       DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
