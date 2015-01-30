/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50610
 Source Host           : localhost
 Source Database       : openfiremar

 Target Server Type    : MySQL
 Target Server Version : 50610
 File Encoding         : utf-8

 Date: 01/29/2015 23:44:18 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ext_applicationConfig`
-- ----------------------------
DROP TABLE IF EXISTS `ext_applicationConfig`;
CREATE TABLE `ext_applicationConfig` (
  `key` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_applicationConfig`
-- ----------------------------
BEGIN;
INSERT INTO `ext_applicationConfig` VALUES ('index_max_id', '0');
COMMIT;

-- ----------------------------
--  Table structure for `ext_appstore`
-- ----------------------------
DROP TABLE IF EXISTS `ext_appstore`;
CREATE TABLE `ext_appstore` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(128) DEFAULT NULL,
  `filePath` varchar(128) DEFAULT NULL,
  `version` varchar(128) DEFAULT NULL,
  `description` varchar(3000) DEFAULT NULL,
  `platform` varchar(32) DEFAULT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ext_feature`
-- ----------------------------
DROP TABLE IF EXISTS `ext_feature`;
CREATE TABLE `ext_feature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `criteria` varchar(255) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_feature`
-- ----------------------------
BEGIN;
INSERT INTO `ext_feature` VALUES ('1', '2015-01-26 12:40:40', '2015-01-26 12:40:45', '1', '111', 'update user', b'1', 'updateUser');
COMMIT;

-- ----------------------------
--  Table structure for `ext_jobLog`
-- ----------------------------
DROP TABLE IF EXISTS `ext_jobLog`;
CREATE TABLE `ext_jobLog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job` varchar(32) DEFAULT NULL,
  `success` bit(1) DEFAULT NULL,
  `exception` varchar(255) DEFAULT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ext_ofHistory`
-- ----------------------------


-- ----------------------------
--  Table structure for `ext_project`
-- ----------------------------
DROP TABLE IF EXISTS `ext_project`;
CREATE TABLE `ext_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `activationCode` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `serialNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_project`
-- ----------------------------
BEGIN;
INSERT INTO `ext_project` VALUES ('7', '2015-01-26 11:48:13', '2015-01-26 11:48:13', '111', '111', '111', '111');
COMMIT;

-- ----------------------------
--  Table structure for `ext_role`
-- ----------------------------
DROP TABLE IF EXISTS `ext_role`;
CREATE TABLE `ext_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `displayName` varchar(64) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `projectId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9324DBD49AFF9907` (`projectId`),
  CONSTRAINT `FK9324DBD49AFF9907` FOREIGN KEY (`projectId`) REFERENCES `ext_project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_role`
-- ----------------------------
BEGIN;
INSERT INTO `ext_role` VALUES ('1', '2015-01-26 12:41:32', '2015-01-26 12:41:32', '超级管理员', '超级管理员', b'1', 'ROLE_ADMIN', null);
COMMIT;

-- ----------------------------
--  Table structure for `ext_rolefeature`
-- ----------------------------
DROP TABLE IF EXISTS `ext_rolefeature`;
CREATE TABLE `ext_rolefeature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `featureId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC9DA68A28F577581` (`featureId`),
  KEY `FKC9DA68A225C502CD` (`roleId`),
  CONSTRAINT `FKC9DA68A225C502CD` FOREIGN KEY (`roleId`) REFERENCES `ext_role` (`id`),
  CONSTRAINT `FKC9DA68A28F577581` FOREIGN KEY (`featureId`) REFERENCES `ext_feature` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ext_user`
-- ----------------------------
DROP TABLE IF EXISTS `ext_user`;
CREATE TABLE `ext_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `accountExpired` bit(1) DEFAULT NULL,
  `accountLocked` bit(1) DEFAULT NULL,
  `authentication` bit(1) DEFAULT NULL,
  `credentialsExpired` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `manager` bit(1) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `userNo` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `projectId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK932647299AFF9907` (`projectId`),
  CONSTRAINT `FK932647299AFF9907` FOREIGN KEY (`projectId`) REFERENCES `ext_project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_user`
-- ----------------------------
BEGIN;
INSERT INTO `ext_user` VALUES ('1', '2015-01-26 11:43:07', '2015-01-26 11:43:10', b'0', b'0', b'1', b'0', null, b'1', null, null, null, null, null, 'admin', 'dcce8cd0dd0fea6f11a197c57ca541a3a645ecbd9d12b098', null, null, 'admin', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `ext_userrole`
-- ----------------------------
DROP TABLE IF EXISTS `ext_userrole`;
CREATE TABLE `ext_userrole` (
  `userId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`userId`,`roleId`),
  KEY `FK7B220B1F25C502CD` (`roleId`),
  KEY `FK7B220B1F2B1A5837` (`userId`),
  CONSTRAINT `FK7B220B1F25C502CD` FOREIGN KEY (`roleId`) REFERENCES `ext_role` (`id`),
  CONSTRAINT `FK7B220B1F2B1A5837` FOREIGN KEY (`userId`) REFERENCES `ext_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofChatHistory`
-- ----------------------------
DROP TABLE IF EXISTS `ofChatHistory`;
CREATE TABLE `ofChatHistory` (
  `fromJID` varchar(255) DEFAULT NULL,
  `toJID` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `messagetime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofExtComponentConf`
-- ----------------------------
DROP TABLE IF EXISTS `ofExtComponentConf`;
CREATE TABLE `ofExtComponentConf` (
  `subdomain` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `wildcard` tinyint(4) NOT NULL,
  `secret` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `permission` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`subdomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofGroup`
-- ----------------------------
DROP TABLE IF EXISTS `ofGroup`;
CREATE TABLE `ofGroup` (
  `groupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`groupName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofGroupProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofGroupProp`;
CREATE TABLE `ofGroupProp` (
  `groupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `propValue` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`groupName`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofGroupUser`
-- ----------------------------
DROP TABLE IF EXISTS `ofGroupUser`;
CREATE TABLE `ofGroupUser` (
  `groupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `administrator` tinyint(4) NOT NULL,
  PRIMARY KEY (`groupName`,`username`,`administrator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofID`
-- ----------------------------
DROP TABLE IF EXISTS `ofID`;
CREATE TABLE `ofID` (
  `idType` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`idType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofID`
-- ----------------------------
BEGIN;
INSERT INTO `ofID` VALUES ('18', '6'), ('19', '13'), ('23', '3'), ('25', '10'), ('26', '2'), ('200', '5');
COMMIT;

-- ----------------------------
--  Table structure for `ofMucAffiliation`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucAffiliation`;
CREATE TABLE `ofMucAffiliation` (
  `roomID` bigint(20) NOT NULL,
  `jid` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `affiliation` tinyint(4) NOT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofMucConversationLog`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucConversationLog`;
CREATE TABLE `ofMucConversationLog` (
  `roomID` bigint(20) NOT NULL,
  `sender` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `logTime` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `body` text CHARACTER SET utf8 COLLATE utf8_bin,
  KEY `ofMucConversationLog_time_idx` (`logTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofMucMember`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucMember`;
CREATE TABLE `ofMucMember` (
  `roomID` bigint(20) NOT NULL,
  `jid` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `firstName` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lastName` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `faqentry` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofMucRoom`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucRoom`;
CREATE TABLE `ofMucRoom` (
  `serviceID` bigint(20) NOT NULL,
  `roomID` bigint(20) NOT NULL,
  `creationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `modificationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `naturalName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lockedDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `emptyDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `canChangeSubject` tinyint(4) NOT NULL,
  `maxUsers` int(11) NOT NULL,
  `publicRoom` tinyint(4) NOT NULL,
  `moderated` tinyint(4) NOT NULL,
  `membersOnly` tinyint(4) NOT NULL,
  `canInvite` tinyint(4) NOT NULL,
  `roomPassword` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `canDiscoverJID` tinyint(4) NOT NULL,
  `logEnabled` tinyint(4) NOT NULL,
  `subject` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `rolesToBroadcast` tinyint(4) NOT NULL,
  `useReservedNick` tinyint(4) NOT NULL,
  `canChangeNick` tinyint(4) NOT NULL,
  `canRegister` tinyint(4) NOT NULL,
  PRIMARY KEY (`serviceID`,`name`),
  KEY `ofMucRoom_roomid_idx` (`roomID`),
  KEY `ofMucRoom_serviceid_idx` (`serviceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofMucRoom`
-- ----------------------------
BEGIN;
INSERT INTO `ofMucRoom` VALUES ('1', '7', '001422244093280', '001422244093280', '111', '111', '', '000000000000000', '001422244093280', '1', '1000', '1', '0', '0', '1', null, '0', '0', '111', '7', '0', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `ofMucRoomProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucRoomProp`;
CREATE TABLE `ofMucRoomProp` (
  `roomID` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `propValue` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`roomID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofMucService`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucService`;
CREATE TABLE `ofMucService` (
  `serviceID` bigint(20) NOT NULL,
  `subdomain` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `isHidden` tinyint(4) NOT NULL,
  PRIMARY KEY (`subdomain`),
  KEY `ofMucService_serviceid_idx` (`serviceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofMucService`
-- ----------------------------
BEGIN;
INSERT INTO `ofMucService` VALUES ('1', 'conference', null, '0');
COMMIT;

-- ----------------------------
--  Table structure for `ofMucServiceProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucServiceProp`;
CREATE TABLE `ofMucServiceProp` (
  `serviceID` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `propValue` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`serviceID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofOffline`
-- ----------------------------
DROP TABLE IF EXISTS `ofOffline`;
CREATE TABLE `ofOffline` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `messageID` bigint(20) NOT NULL,
  `creationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `messageSize` int(11) NOT NULL,
  `stanza` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`messageID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofPresence`
-- ----------------------------
DROP TABLE IF EXISTS `ofPresence`;
CREATE TABLE `ofPresence` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `offlinePresence` text CHARACTER SET utf8 COLLATE utf8_bin,
  `offlineDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofPrivacyList`
-- ----------------------------
DROP TABLE IF EXISTS `ofPrivacyList`;
CREATE TABLE `ofPrivacyList` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `isDefault` tinyint(4) NOT NULL,
  `list` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`name`),
  KEY `ofPrivacyList_default_idx` (`username`,`isDefault`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofPrivate`
-- ----------------------------
DROP TABLE IF EXISTS `ofPrivate`;
CREATE TABLE `ofPrivate` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `namespace` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `privateData` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`name`,`namespace`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofProperty`
-- ----------------------------
DROP TABLE IF EXISTS `ofProperty`;
CREATE TABLE `ofProperty` (
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `propValue` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofProperty`
-- ----------------------------
BEGIN;
INSERT INTO `ofProperty` VALUES ('cumulus.path.default', 0x2f55736572732f6c6968616f2f446f776e6c6f6164732f6f70656e666972655f7372632f7461726765742f6f70656e666972652f706c7567696e732f726564666972652f63756d756c75732f43756d756c75735365727665722e657865), ('passwordKey', 0x56364e3832494876793948366b4371), ('provider.admin.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e61646d696e2e44656661756c7441646d696e50726f7669646572), ('provider.auth.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e617574682e44656661756c744175746850726f7669646572), ('provider.group.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e67726f75702e44656661756c7447726f757050726f7669646572), ('provider.lockout.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e6c6f636b6f75742e44656661756c744c6f636b4f757450726f7669646572), ('provider.securityAudit.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e73656375726974792e44656661756c745365637572697479417564697450726f7669646572), ('provider.user.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e757365722e44656661756c745573657250726f7669646572), ('provider.vcard.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e76636172642e44656661756c74564361726450726f7669646572), ('update.lastCheck', 0x31343232313730333639303631), ('xmpp.auth.anonymous', 0x74727565), ('xmpp.domain', 0x626f676f6e), ('xmpp.session.conflict-limit', 0x30), ('xmpp.socket.ssl.active', 0x74727565);
COMMIT;

-- ----------------------------
--  Table structure for `ofPubsubAffiliation`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubAffiliation`;
CREATE TABLE `ofPubsubAffiliation` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `affiliation` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofPubsubAffiliation`
-- ----------------------------
BEGIN;
INSERT INTO `ofPubsubAffiliation` VALUES ('pubsub', '', 'lihaos-macbook-pro.local', 'owner');
COMMIT;

-- ----------------------------
--  Table structure for `ofPubsubDefaultConf`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubDefaultConf`;
CREATE TABLE `ofPubsubDefaultConf` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `leaf` tinyint(4) NOT NULL,
  `deliverPayloads` tinyint(4) NOT NULL,
  `maxPayloadSize` int(11) NOT NULL,
  `persistItems` tinyint(4) NOT NULL,
  `maxItems` int(11) NOT NULL,
  `notifyConfigChanges` tinyint(4) NOT NULL,
  `notifyDelete` tinyint(4) NOT NULL,
  `notifyRetract` tinyint(4) NOT NULL,
  `presenceBased` tinyint(4) NOT NULL,
  `sendItemSubscribe` tinyint(4) NOT NULL,
  `publisherModel` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `subscriptionEnabled` tinyint(4) NOT NULL,
  `accessModel` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `language` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `replyPolicy` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `associationPolicy` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `maxLeafNodes` int(11) NOT NULL,
  PRIMARY KEY (`serviceID`,`leaf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofPubsubDefaultConf`
-- ----------------------------
BEGIN;
INSERT INTO `ofPubsubDefaultConf` VALUES ('pubsub', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', 'publishers', '1', 'open', 'English', null, 'all', '-1'), ('pubsub', '1', '1', '5120', '0', '-1', '1', '1', '1', '0', '1', 'publishers', '1', 'open', 'English', null, 'all', '-1');
COMMIT;

-- ----------------------------
--  Table structure for `ofPubsubItem`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubItem`;
CREATE TABLE `ofPubsubItem` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `creationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `payload` mediumtext CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`serviceID`,`nodeID`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofPubsubNode`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubNode`;
CREATE TABLE `ofPubsubNode` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `leaf` tinyint(4) NOT NULL,
  `creationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `modificationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `parent` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `deliverPayloads` tinyint(4) NOT NULL,
  `maxPayloadSize` int(11) DEFAULT NULL,
  `persistItems` tinyint(4) DEFAULT NULL,
  `maxItems` int(11) DEFAULT NULL,
  `notifyConfigChanges` tinyint(4) NOT NULL,
  `notifyDelete` tinyint(4) NOT NULL,
  `notifyRetract` tinyint(4) NOT NULL,
  `presenceBased` tinyint(4) NOT NULL,
  `sendItemSubscribe` tinyint(4) NOT NULL,
  `publisherModel` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `subscriptionEnabled` tinyint(4) NOT NULL,
  `configSubscription` tinyint(4) NOT NULL,
  `accessModel` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `payloadType` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `bodyXSLT` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `dataformXSLT` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `language` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `replyPolicy` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `associationPolicy` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `maxLeafNodes` int(11) DEFAULT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofPubsubNode`
-- ----------------------------
BEGIN;
INSERT INTO `ofPubsubNode` VALUES ('pubsub', '', '0', '001418224454074', '001418224454074', null, '0', '0', '0', '0', '1', '1', '1', '0', '0', 'publishers', '1', '0', 'open', '', '', '', 'lihaos-macbook-pro.local', '', 'English', '', null, 'all', '-1');
COMMIT;

-- ----------------------------
--  Table structure for `ofPubsubNodeGroups`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubNodeGroups`;
CREATE TABLE `ofPubsubNodeGroups` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `rosterGroup` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  KEY `ofPubsubNodeGroups_idx` (`serviceID`,`nodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofPubsubNodeJIDs`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubNodeJIDs`;
CREATE TABLE `ofPubsubNodeJIDs` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `associationType` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofPubsubSubscription`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubSubscription`;
CREATE TABLE `ofPubsubSubscription` (
  `serviceID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `state` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `deliver` tinyint(4) NOT NULL,
  `digest` tinyint(4) NOT NULL,
  `digest_frequency` int(11) NOT NULL,
  `expire` char(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `includeBody` tinyint(4) NOT NULL,
  `showValues` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `subscriptionType` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `subscriptionDepth` tinyint(4) NOT NULL,
  `keyword` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofRemoteServerConf`
-- ----------------------------
DROP TABLE IF EXISTS `ofRemoteServerConf`;
CREATE TABLE `ofRemoteServerConf` (
  `xmppDomain` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `remotePort` int(11) DEFAULT NULL,
  `permission` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`xmppDomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofRoster`
-- ----------------------------
DROP TABLE IF EXISTS `ofRoster`;
CREATE TABLE `ofRoster` (
  `rosterID` bigint(20) NOT NULL,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `jid` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sub` tinyint(4) NOT NULL,
  `ask` tinyint(4) NOT NULL,
  `recv` tinyint(4) NOT NULL,
  `nick` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`rosterID`),
  KEY `ofRoster_unameid_idx` (`username`),
  KEY `ofRoster_jid_idx` (`jid`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofRoster`
-- ----------------------------
BEGIN;
INSERT INTO `ofRoster` VALUES ('1', 'lihao', 'lihao@lihaos-macbook-pro.local', '3', '-1', '-1', 'adminsssss'), ('2', 'admin', 'admin@lihaos-macbook-pro.local', '3', '-1', '-1', 'lihao');
COMMIT;

-- ----------------------------
--  Table structure for `ofRosterGroups`
-- ----------------------------
DROP TABLE IF EXISTS `ofRosterGroups`;
CREATE TABLE `ofRosterGroups` (
  `rosterID` bigint(20) NOT NULL,
  `rank` tinyint(4) NOT NULL,
  `groupName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`rosterID`,`rank`),
  KEY `ofRosterGroup_rosterid_idx` (`rosterID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofRosterGroups`
-- ----------------------------
BEGIN;
INSERT INTO `ofRosterGroups` VALUES ('1', '0', '朋友'), ('2', '0', '看圣诞节');
COMMIT;

-- ----------------------------
--  Table structure for `ofSASLAuthorized`
-- ----------------------------
DROP TABLE IF EXISTS `ofSASLAuthorized`;
CREATE TABLE `ofSASLAuthorized` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `principal` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`principal`(200))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofSecurityAuditLog`
-- ----------------------------
DROP TABLE IF EXISTS `ofSecurityAuditLog`;
CREATE TABLE `ofSecurityAuditLog` (
  `msgID` bigint(20) NOT NULL,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `entryStamp` bigint(20) NOT NULL,
  `summary` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `node` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `details` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`msgID`),
  KEY `ofSecurityAuditLog_tstamp_idx` (`entryStamp`),
  KEY `ofSecurityAuditLog_uname_idx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofUser`
-- ----------------------------
DROP TABLE IF EXISTS `ofUser`;
CREATE TABLE `ofUser` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `plainPassword` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `encryptedPassword` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `creationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `modificationDate` char(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`),
  KEY `ofUser_cDate_idx` (`creationDate`),
  KEY `username_idx` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofUser`
-- ----------------------------
BEGIN;
INSERT INTO `ofUser` VALUES ('admin', null, 'dcce8cd0dd0fea6f11a197c57ca541a3a645ecbd9d12b098', 'Administrator', 'admin@example.com', '001422170352765', '0');
COMMIT;

-- ----------------------------
--  Table structure for `ofUserFlag`
-- ----------------------------
DROP TABLE IF EXISTS `ofUserFlag`;
CREATE TABLE `ofUserFlag` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `startTime` char(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `endTime` char(15) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`username`,`name`),
  KEY `ofUserFlag_sTime_idx` (`startTime`),
  KEY `ofUserFlag_eTime_idx` (`endTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofUserProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofUserProp`;
CREATE TABLE `ofUserProp` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `propValue` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofVCard`
-- ----------------------------
DROP TABLE IF EXISTS `ofVCard`;
CREATE TABLE `ofVCard` (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `vcard` mediumtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ofVersion`
-- ----------------------------
DROP TABLE IF EXISTS `ofVersion`;
CREATE TABLE `ofVersion` (
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ofVersion`
-- ----------------------------
BEGIN;
INSERT INTO `ofVersion` VALUES ('openfire', '21');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
