
create database android_sample default character set utf8;
use android_sample;
CREATE TABLE IF NOT EXISTS `members` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `userID` varchar(60) NOT NULL,
  `userNM` varchar(30) NOT NULL,
  `passwd` varchar(80) NOT NULL,
  `salt` varchar(20) NOT NULL,
  `telNO` varchar(16) DEFAULT NULL,
  `mobileNO` varchar(34) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `phoneSE` varchar(50) DEFAULT NULL,
  `OStype` tinyint(2) NOT NULL DEFAULT '0',
  `admin` tinyint(4) NOT NULL DEFAULT '0',
  `auth` tinyint(4) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL DEFAULT '0',
  `reg_date` datetime DEFAULT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `userID` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

use mysql;
create user androidsample@localhost;
grant all privileges on android_sample.* to androidsample@localhost identified by 'android!#%';
flush privileges;

