/*
SQLyog Ultimate v10.51 
MySQL - 5.5.29-ndb-7.2.10-cluster-gpl-log : Database - cmpp
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `cmpp_deliver` */

DROP TABLE IF EXISTS `cmpp_deliver`;

CREATE TABLE `cmpp_deliver` (
  `id` varchar(64) NOT NULL,
  `dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `msgId` bigint(20) DEFAULT NULL,
  `destId` varchar(254) DEFAULT NULL COMMENT ' 21',
  `serviceId` varchar(254) DEFAULT NULL COMMENT ' 10',
  `tp_pid` tinyint(4) DEFAULT NULL,
  `tp_udhi` tinyint(4) DEFAULT NULL,
  `msgFmt` tinyint(4) DEFAULT NULL,
  `srcTerminalId` varchar(254) DEFAULT NULL COMMENT ' 21',
  `srcTerminalType` tinyint(4) DEFAULT NULL,
  `registeredDelivery` tinyint(4) DEFAULT NULL,
  `msgLength` int(11) DEFAULT NULL,
  `msgContent` varchar(512) DEFAULT NULL COMMENT ' byte msgLength;\n             msgLength',
  `linkId` varchar(254) DEFAULT NULL COMMENT ' 8',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cmpp_report` */

DROP TABLE IF EXISTS `cmpp_report`;

CREATE TABLE `cmpp_report` (
  `id` varchar(64) NOT NULL,
  `dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `msgId` bigint(20) DEFAULT NULL COMMENT ' report begin',
  `stat` varchar(254) DEFAULT NULL,
  `submitTime` varchar(254) DEFAULT NULL,
  `doneTime` varchar(254) DEFAULT NULL,
  `destTerminalId` varchar(254) DEFAULT NULL,
  `smscSequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `msgId` (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cmpp_submit` */

DROP TABLE IF EXISTS `cmpp_submit`;

CREATE TABLE `cmpp_submit` (
  `id` varchar(64) NOT NULL,
  `dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `msgId` bigint(20) DEFAULT NULL,
  `pkTotal` tinyint(4) DEFAULT NULL,
  `pkNumber` tinyint(4) DEFAULT NULL,
  `registeredDelivery` tinyint(4) DEFAULT NULL,
  `msgLevel` tinyint(4) DEFAULT NULL,
  `serviceId` varchar(254) DEFAULT NULL COMMENT ' 10',
  `feeUserType` tinyint(4) DEFAULT NULL,
  `feeTerminalId` varchar(254) DEFAULT NULL COMMENT ' 21',
  `feeTerminalType` tinyint(4) DEFAULT NULL,
  `tp_pid` tinyint(4) DEFAULT NULL,
  `tp_udhi` tinyint(4) DEFAULT NULL,
  `msgFmt` tinyint(4) DEFAULT NULL,
  `msgSrc` varchar(254) DEFAULT NULL COMMENT ' 6',
  `feeType` varchar(254) DEFAULT NULL COMMENT ' 2',
  `feeCode` varchar(254) DEFAULT NULL COMMENT ' 6',
  `vaildTime` varchar(254) DEFAULT NULL COMMENT ' 17',
  `atTime` varchar(254) DEFAULT NULL COMMENT ' 17',
  `srcId` varchar(254) DEFAULT NULL COMMENT ' 21',
  `destUsrTl` tinyint(4) DEFAULT NULL,
  `destTerminalType` tinyint(4) DEFAULT NULL,
  `msgLength` int(11) DEFAULT NULL,
  `msgContent` varchar(512) DEFAULT NULL COMMENT ' byte msgLength; // 1',
  `linkId` varchar(254) DEFAULT NULL COMMENT ' 8',
  `result` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `msgId` (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `cmpp_submit_destid` */

DROP TABLE IF EXISTS `cmpp_submit_destid`;

CREATE TABLE `cmpp_submit_destid` (
  `submitid` varchar(64) NOT NULL,
  `destTerminalId` varchar(32) NOT NULL,
  KEY `fk_submitid` (`submitid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `vcmpp_report` */

DROP TABLE IF EXISTS `vcmpp_report`;

/*!50001 DROP VIEW IF EXISTS `vcmpp_report` */;
/*!50001 DROP TABLE IF EXISTS `vcmpp_report` */;

/*!50001 CREATE TABLE  `vcmpp_report`(
 `id` varchar(64) ,
 `dt` timestamp ,
 `msgId` bigint(20) ,
 `pkTotal` tinyint(4) ,
 `pkNumber` tinyint(4) ,
 `registeredDelivery` tinyint(4) ,
 `msgLevel` tinyint(4) ,
 `serviceId` varchar(254) ,
 `feeUserType` tinyint(4) ,
 `feeTerminalId` varchar(254) ,
 `feeTerminalType` tinyint(4) ,
 `tp_pid` tinyint(4) ,
 `tp_udhi` tinyint(4) ,
 `msgFmt` tinyint(4) ,
 `msgSrc` varchar(254) ,
 `feeType` varchar(254) ,
 `feeCode` varchar(254) ,
 `vaildTime` varchar(254) ,
 `atTime` varchar(254) ,
 `srcId` varchar(254) ,
 `destUsrTl` tinyint(4) ,
 `destTerminalType` tinyint(4) ,
 `msgLength` int(11) ,
 `msgContent` varchar(512) ,
 `linkId` varchar(254) ,
 `result` int(11) ,
 `submitid` varchar(64) ,
 `destTerminalId` varchar(32) 
)*/;

/*View structure for view vcmpp_report */

/*!50001 DROP TABLE IF EXISTS `vcmpp_report` */;
/*!50001 DROP VIEW IF EXISTS `vcmpp_report` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vcmpp_report` AS select `cmpp_submit`.`id` AS `id`,`cmpp_submit`.`dt` AS `dt`,`cmpp_submit`.`msgId` AS `msgId`,`cmpp_submit`.`pkTotal` AS `pkTotal`,`cmpp_submit`.`pkNumber` AS `pkNumber`,`cmpp_submit`.`registeredDelivery` AS `registeredDelivery`,`cmpp_submit`.`msgLevel` AS `msgLevel`,`cmpp_submit`.`serviceId` AS `serviceId`,`cmpp_submit`.`feeUserType` AS `feeUserType`,`cmpp_submit`.`feeTerminalId` AS `feeTerminalId`,`cmpp_submit`.`feeTerminalType` AS `feeTerminalType`,`cmpp_submit`.`tp_pid` AS `tp_pid`,`cmpp_submit`.`tp_udhi` AS `tp_udhi`,`cmpp_submit`.`msgFmt` AS `msgFmt`,`cmpp_submit`.`msgSrc` AS `msgSrc`,`cmpp_submit`.`feeType` AS `feeType`,`cmpp_submit`.`feeCode` AS `feeCode`,`cmpp_submit`.`vaildTime` AS `vaildTime`,`cmpp_submit`.`atTime` AS `atTime`,`cmpp_submit`.`srcId` AS `srcId`,`cmpp_submit`.`destUsrTl` AS `destUsrTl`,`cmpp_submit`.`destTerminalType` AS `destTerminalType`,`cmpp_submit`.`msgLength` AS `msgLength`,`cmpp_submit`.`msgContent` AS `msgContent`,`cmpp_submit`.`linkId` AS `linkId`,`cmpp_submit`.`result` AS `result`,`cmpp_submit_destid`.`submitid` AS `submitid`,`cmpp_submit_destid`.`destTerminalId` AS `destTerminalId` from (`cmpp_submit` join `cmpp_submit_destid`) where ((`cmpp_submit`.`id` = `cmpp_submit_destid`.`submitid`) and exists(select 1 from `cmpp_report` where ((`cmpp_submit_destid`.`destTerminalId` = `cmpp_report`.`destTerminalId`) and (`cmpp_submit`.`msgId` = `cmpp_report`.`msgId`) and (`cmpp_report`.`stat` = 'DELIVRD')))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
