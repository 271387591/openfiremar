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

 Date: 01/02/2015 22:00:52 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ext_feature`
-- ----------------------------
DROP TABLE IF EXISTS `ext_feature`;
CREATE TABLE `ext_feature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `criteria` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8 DEFAULT NULL,
  `displayName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ext_feature`
-- ----------------------------
BEGIN;
INSERT INTO `ext_feature` VALUES ('1', '2013-01-27 11:27:03', '2014-07-25 13:51:42', 'user', '添加角色', '添加角色', b'1', 'addRole'), ('2', '2013-01-27 11:27:03', '2014-07-25 13:51:49', 'user', '编辑角色', '编辑角色', b'1', 'updateRole'), ('3', '2013-01-27 11:27:03', '2014-07-25 13:52:04', 'user', '删除角色', '删除角色', b'1', 'deleteRole'), ('4', '2014-07-25 13:46:30', '2014-07-25 13:46:30', 'user', '添加用户', '添加用户', b'1', 'addUser'), ('5', '2014-07-25 13:47:55', '2014-07-25 13:47:55', 'user', '修改用户', '修改用户', b'1', 'updateUser'), ('6', '2014-07-25 13:48:38', '2014-07-25 13:48:38', 'user', '修改用户密码', '修改用户密码', b'1', 'updateUserPassword'), ('7', '2014-07-25 13:49:06', '2014-07-25 13:49:06', 'user', '锁定用户', '锁定用户', b'1', 'lockUser'), ('8', '2014-07-25 13:49:31', '2014-07-25 13:49:31', 'user', '解锁用户', '解锁用户', b'1', 'unLockUser'), ('9', '2014-07-25 13:49:59', '2014-07-25 13:49:59', 'user', '禁用用户', '禁用用户', b'1', 'disableUser'), ('10', '2014-07-25 13:50:23', '2014-07-25 13:50:23', 'user', '解禁用户', '解禁用户', b'1', 'enableUser'), ('11', '2014-11-24 22:24:33', '2014-11-24 22:24:36', 'user', '显示用户模块', '显示用户模块', b'1', 'showUserManager'), ('12', '2014-11-24 22:25:18', '2014-11-24 22:25:20', 'system', '显示基础数据模块', '显示基础数据模块', b'1', 'showSystemDataManager'), ('13', '2014-11-24 22:25:36', '2014-11-24 22:25:39', 'forms', '显示表单模块', '显示表单模块', b'1', 'showFlowFormManager'), ('14', '2014-11-24 22:26:15', '2014-11-24 22:26:17', 'flows', '显示流程模块', '显示流程模块', b'1', 'showFlowManager'), ('15', '2014-11-25 16:08:25', '2014-11-25 16:08:27', 'flows', '添加流程', '添加流程', b'1', 'addProcess'), ('16', '2014-11-25 16:24:38', '2014-11-25 16:24:42', 'flows', '删除流程', '删除流程', b'1', 'deleteProcess'), ('17', '2014-11-25 16:26:03', '2014-11-25 16:26:05', 'flows', '流程授权', '流程授权', b'1', 'authorizationProcess'), ('18', '2014-11-25 16:29:23', '2014-11-25 16:29:25', 'flows', '编辑流程', '编辑流程', b'1', 'updateProcess'), ('19', '2014-11-25 20:09:04', '2014-11-25 20:09:06', 'flows', '部署流程', '部署流程', b'1', 'deployProcess'), ('20', '2014-11-25 20:25:43', '2014-11-25 20:25:45', 'forms', '发布表单', '发布表单', b'1', 'publishForm'), ('21', '2014-11-25 20:32:31', '2014-11-25 20:32:33', 'forms', '编辑表单', '编辑表单', b'1', 'updateForm'), ('22', '2014-11-25 20:34:31', '2014-11-25 20:34:33', 'forms', '添加表单', '添加表单', b'1', 'addForm'), ('23', '2014-11-25 20:35:14', '2014-11-25 20:35:16', 'forms', '删除表单', '删除表单', b'1', 'updateForm'), ('24', '2014-11-25 21:41:29', '2014-11-25 21:41:31', 'flows', '解除流程权限', '解除流程权限', b'1', 'disAuthorizationProcess');
COMMIT;

-- ----------------------------
--  Table structure for `ext_ofHistory`
-- ----------------------------
DROP TABLE IF EXISTS `ext_ofHistory`;
CREATE TABLE `ext_ofHistory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fromId` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `toId` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `type` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `message` mediumtext CHARACTER SET utf8,
  `createDate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fromIdIndex` (`fromId`) USING BTREE,
  KEY `toIdIndex` (`toId`) USING BTREE,
  KEY `createDateIndex` (`createDate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1020405 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ext_project`
-- ----------------------------
DROP TABLE IF EXISTS `ext_project`;
CREATE TABLE `ext_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `serialNumber` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` tinytext,
  `peoples` int(10) DEFAULT NULL,
  `activationCode` varchar(32) DEFAULT NULL,
  `mucRoomId` bigint(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mucRoomId` (`mucRoomId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_project`
-- ----------------------------
BEGIN;
INSERT INTO `ext_project` VALUES ('1', '23', '工程1', '的', '3', '111', '1', '2015-01-02 12:45:46', '2015-01-02 12:45:51');
COMMIT;

-- ----------------------------
--  Table structure for `ext_project_user`
-- ----------------------------
DROP TABLE IF EXISTS `ext_project_user`;
CREATE TABLE `ext_project_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `projectId` bigint(20) NOT NULL,
  `manager` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`) USING BTREE,
  KEY `projectId` (`projectId`) USING BTREE,
  CONSTRAINT `ext_project_user_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `ext_user` (`id`),
  CONSTRAINT `ext_project_user_ibfk_2` FOREIGN KEY (`projectId`) REFERENCES `ext_project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ext_project_user`
-- ----------------------------
BEGIN;
INSERT INTO `ext_project_user` VALUES ('1', '7', '1', b'0');
COMMIT;

-- ----------------------------
--  Table structure for `ext_role`
-- ----------------------------
DROP TABLE IF EXISTS `ext_role`;
CREATE TABLE `ext_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `displayName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `systemViewId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK26F4965BE75AC8` (`systemViewId`),
  CONSTRAINT `FK26F4965BE75AC8` FOREIGN KEY (`systemViewId`) REFERENCES `ext_systemview` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ext_role`
-- ----------------------------
BEGIN;
INSERT INTO `ext_role` VALUES ('-2', '2013-01-22 16:03:26', '2013-01-22 16:03:28', '分公司用户，录入分公司的销售数据', '分公司用户', b'1', 'ROLE_USER', '2'), ('-1', '2013-01-22 16:03:26', '2014-07-25 14:22:04', '超级管理员，具有管理的全部权限', '超级管理员', b'1', 'ROLE_ADMIN', '1'), ('1', '2014-11-25 14:47:26', '2014-11-25 14:47:26', '爱死', '1拉萨的减肥', null, 'role', '1'), ('2', '2014-11-25 15:22:45', '2014-11-25 22:20:35', '', 'asd', b'1', 'rlo1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `ext_rolefeature`
-- ----------------------------
DROP TABLE IF EXISTS `ext_rolefeature`;
CREATE TABLE `ext_rolefeature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `featureId` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4EA9E980C0584BC2` (`featureId`),
  KEY `FK4EA9E980CA28BC2C` (`roleId`),
  CONSTRAINT `FK4EA9E980C0584BC2` FOREIGN KEY (`featureId`) REFERENCES `ext_feature` (`id`),
  CONSTRAINT `FK4EA9E980CA28BC2C` FOREIGN KEY (`roleId`) REFERENCES `ext_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ext_rolefeature`
-- ----------------------------
BEGIN;
INSERT INTO `ext_rolefeature` VALUES ('1', '8', '2');
COMMIT;

-- ----------------------------
--  Table structure for `ext_systemview`
-- ----------------------------
DROP TABLE IF EXISTS `ext_systemview`;
CREATE TABLE `ext_systemview` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createDate` datetime NOT NULL,
  `lastUpdateDate` datetime DEFAULT NULL,
  `context` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ext_systemview`
-- ----------------------------
BEGIN;
INSERT INTO `ext_systemview` VALUES ('1', '2013-01-27 11:27:03', '2013-06-27 11:27:25', 'manager', b'1', '管理员环境', 'desktop'), ('2', '2013-01-27 11:27:03', '2013-06-27 11:27:25', 'user', b'1', '用户环境', 'userview');
COMMIT;

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
  `address` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `postalCode` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `credentialsExpired` bit(1) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `firstName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `lastName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `passwordHint` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `website` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `defaultRoleId` bigint(20) DEFAULT NULL,
  `nickName` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `authentication` bit(1) DEFAULT NULL,
  `projectId` bigint(20) DEFAULT NULL,
  `userNo` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile` (`mobile`),
  UNIQUE KEY `username` (`username`),
  KEY `FK285FEB6FF91DAD` (`defaultRoleId`),
  KEY `projectId` (`projectId`) USING BTREE,
  CONSTRAINT `ext_user_ibfk_1` FOREIGN KEY (`projectId`) REFERENCES `ext_project` (`id`),
  CONSTRAINT `FK285FEB6FF91DAD` FOREIGN KEY (`defaultRoleId`) REFERENCES `ext_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ext_user`
-- ----------------------------
BEGIN;
INSERT INTO `ext_user` VALUES ('1', '2013-01-27 11:27:03', '2013-01-27 11:27:25', b'0', b'0', 'Denver', 'Denver', 'US', '80210', 'CO', b'0', 'admin@ozstrategy.com', b'1', '李', 'M', '浩admin', '18583910581', 'bf7216f2f59e55e357867168e8a412a867f37e656188931a', '', '110', 'admin', '1', 'http://www.cnblogs.com/hk-jiujiu', '-1', null, b'1', null, null), ('2', '2013-01-27 11:27:03', '2013-01-27 11:27:25', b'0', b'0', 'Denver', 'Denver', 'US', '80210', 'CO', b'0', 'user@ozstrategy.com', b'1', '李', 'M', '浩user', '18583910582', '536c0b339345616c1b33caf454454d8b8a190d6c', '', '110', 'user', '1', 'http://www.cnblogs.com/hk-jiujiu', '-2', null, b'1', null, null), ('3', '2013-01-27 11:27:03', '2013-01-27 11:27:25', b'0', b'0', 'Denver', 'Denver', 'US', '80210', 'CO', b'0', 'hr@ozstrategy.com', b'1', '李', 'M', '浩hr', '18583910583', '536c0b339345616c1b33caf454454d8b8a190d6c', '', '110', 'hr', '1', 'http://www.cnblogs.com/hk-jiujiu', '-2', null, b'1', null, null), ('4', '2013-01-27 11:27:03', '2013-01-27 11:27:25', b'0', b'0', 'Denver', 'Denver', 'US', '80210', 'CO', b'0', 'dep@ozstrategy.com', b'1', '李', 'M', '浩dep', '18583910584', '536c0b339345616c1b33caf454454d8b8a190d6c', '', '110', 'dep', '1', 'http://www.cnblogs.com/hk-jiujiu', '-2', null, b'1', null, null), ('5', '2014-11-25 15:27:52', '2014-11-25 15:27:52', b'0', b'0', null, null, null, null, null, b'0', '33@qq.com', b'1', 'sd', 'M', 'sdf', '13223343212', '3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d', null, null, 'er', '1', null, '2', null, b'1', null, null), ('6', '2014-12-11 15:59:33', '2014-12-11 15:59:33', b'0', b'0', null, null, null, null, null, b'0', '3333@qq.com', b'1', 'sd', 'M', 'sdf', '13444444444', 'bf7216f2f59e55e357867168e8a412a867f37e656188931a', null, null, 'admin1', '1', null, '2', null, b'1', null, null), ('7', '2015-01-02 12:46:19', '2015-01-02 12:46:19', b'0', b'0', null, null, null, null, null, b'0', null, b'1', null, null, null, null, '51ece0908cd1df19156b3118ddcb6a8941b3888e1719cbef', null, null, 'tomcat2', '1', null, '2', 'lksdf', b'0', '1', '');
COMMIT;

-- ----------------------------
--  Table structure for `ext_userrole`
-- ----------------------------
DROP TABLE IF EXISTS `ext_userrole`;
CREATE TABLE `ext_userrole` (
  `userId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`userId`,`roleId`),
  KEY `FKF3F76701CA28BC2C` (`roleId`),
  KEY `FKF3F76701CF7E1196` (`userId`),
  CONSTRAINT `FKF3F76701CA28BC2C` FOREIGN KEY (`roleId`) REFERENCES `ext_role` (`id`),
  CONSTRAINT `FKF3F76701CF7E1196` FOREIGN KEY (`userId`) REFERENCES `ext_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ext_userrole`
-- ----------------------------
BEGIN;
INSERT INTO `ext_userrole` VALUES ('2', '-2'), ('3', '-2'), ('4', '-2'), ('1', '-1'), ('5', '2'), ('6', '2'), ('7', '2');
COMMIT;

-- ----------------------------
--  Table structure for `ofChatHistory`
-- ----------------------------
DROP TABLE IF EXISTS `ofChatHistory`;
CREATE TABLE `ofChatHistory` (
  `fromJID` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `toJID` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `messagetime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofChatHistory`
-- ----------------------------
BEGIN;
INSERT INTO `ofChatHistory` VALUES ('admin@lihaos-macbook-pro.local', '里撒地方@conference.lihaos-macbook-pro.local', '撒旦', '2014-12-11 00:31:06'), ('里撒地方@conference.lihaos-macbook-pro.local', 'admin@lihaos-macbook-pro.local', '撒旦', '2014-12-11 00:31:06'), ('admin@lihaos-macbook-pro.local', '里撒地方@conference.lihaos-macbook-pro.local', '大大大', '2014-12-11 00:32:39'), ('里撒地方@conference.lihaos-macbook-pro.local', 'admin@lihaos-macbook-pro.local', '大大大', '2014-12-11 00:32:45'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '撒旦飞洒发', '2014-12-11 00:35:47'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '阿斯达飞撒旦', '2014-12-11 00:36:02'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '撒旦法士大夫', '2014-12-11 00:36:04'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '撒旦飞洒', '2014-12-11 00:36:05'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', 'asdfsa ', '2014-12-11 00:36:06'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '啊啥地方萨芬', '2014-12-11 00:36:08'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '啦啦啦啦', '2014-12-11 00:38:39'), ('admin@lihaos-macbook-pro.local', '士大夫@conference.lihaos-macbook-pro.local', '立刻就凉快', '2014-12-11 00:41:01'), ('士大夫@conference.lihaos-macbook-pro.local', 'admin@lihaos-macbook-pro.local', ';\\ ', '2014-12-11 00:45:21');
COMMIT;

-- ----------------------------
--  Table structure for `ofExtComponentConf`
-- ----------------------------
DROP TABLE IF EXISTS `ofExtComponentConf`;
CREATE TABLE `ofExtComponentConf` (
  `subdomain` varchar(255) COLLATE utf8_bin NOT NULL,
  `wildcard` tinyint(4) NOT NULL,
  `secret` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `permission` varchar(10) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`subdomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofGroup`
-- ----------------------------
DROP TABLE IF EXISTS `ofGroup`;
CREATE TABLE `ofGroup` (
  `groupName` varchar(50) COLLATE utf8_bin NOT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`groupName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofGroup`
-- ----------------------------
BEGIN;
INSERT INTO `ofGroup` VALUES ('g1', 'sdf'), ('g2', 'kl');
COMMIT;

-- ----------------------------
--  Table structure for `ofGroupProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofGroupProp`;
CREATE TABLE `ofGroupProp` (
  `groupName` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `propValue` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`groupName`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofGroupProp`
-- ----------------------------
BEGIN;
INSERT INTO `ofGroupProp` VALUES ('g2', 'sharedRoster.displayName', ''), ('g2', 'sharedRoster.groupList', ''), ('g2', 'sharedRoster.showInRoster', 0x6e6f626f6479);
COMMIT;

-- ----------------------------
--  Table structure for `ofGroupUser`
-- ----------------------------
DROP TABLE IF EXISTS `ofGroupUser`;
CREATE TABLE `ofGroupUser` (
  `groupName` varchar(50) COLLATE utf8_bin NOT NULL,
  `username` varchar(100) COLLATE utf8_bin NOT NULL,
  `administrator` tinyint(4) NOT NULL,
  PRIMARY KEY (`groupName`,`username`,`administrator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofGroupUser`
-- ----------------------------
BEGIN;
INSERT INTO `ofGroupUser` VALUES ('g2', 'admin', '0'), ('g2', 'lihao', '0');
COMMIT;

-- ----------------------------
--  Table structure for `ofID`
-- ----------------------------
DROP TABLE IF EXISTS `ofID`;
CREATE TABLE `ofID` (
  `idType` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`idType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofID`
-- ----------------------------
BEGIN;
INSERT INTO `ofID` VALUES ('18', '6'), ('19', '3'), ('23', '3'), ('25', '10'), ('26', '2'), ('200', '5');
COMMIT;

-- ----------------------------
--  Table structure for `ofMucAffiliation`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucAffiliation`;
CREATE TABLE `ofMucAffiliation` (
  `roomID` bigint(20) NOT NULL,
  `jid` text COLLATE utf8_bin NOT NULL,
  `affiliation` tinyint(4) NOT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofMucAffiliation`
-- ----------------------------
BEGIN;
INSERT INTO `ofMucAffiliation` VALUES ('1', 0x61646d696e406c6968616f732d6d6163626f6f6b2d70726f2e6c6f63616c, '10'), ('2', 0x6c6968616f406c6968616f732d6d6163626f6f6b2d70726f2e6c6f63616c, '10');
COMMIT;

-- ----------------------------
--  Table structure for `ofMucConversationLog`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucConversationLog`;
CREATE TABLE `ofMucConversationLog` (
  `roomID` bigint(20) NOT NULL,
  `sender` text COLLATE utf8_bin NOT NULL,
  `nickname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logTime` char(15) COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `body` text COLLATE utf8_bin,
  KEY `ofMucConversationLog_time_idx` (`logTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofMucMember`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucMember`;
CREATE TABLE `ofMucMember` (
  `roomID` bigint(20) NOT NULL,
  `jid` text COLLATE utf8_bin NOT NULL,
  `nickname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `firstName` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `lastName` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `faqentry` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`roomID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofMucMember`
-- ----------------------------
BEGIN;
INSERT INTO `ofMucMember` VALUES ('1', 0x61646d696e406c6968616f732d6d6163626f6f6b2d70726f2e6c6f63616c, 'admin', 's', 'd', null, 'sss@qq.com', null), ('1', 0x6c6968616f406c6968616f732d6d6163626f6f6b2d70726f2e6c6f63616c, '', null, null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `ofMucRoom`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucRoom`;
CREATE TABLE `ofMucRoom` (
  `serviceID` bigint(20) NOT NULL,
  `roomID` bigint(20) NOT NULL,
  `creationDate` char(15) COLLATE utf8_bin NOT NULL,
  `modificationDate` char(15) COLLATE utf8_bin NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `naturalName` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `lockedDate` char(15) COLLATE utf8_bin NOT NULL,
  `emptyDate` char(15) COLLATE utf8_bin DEFAULT NULL,
  `canChangeSubject` tinyint(4) NOT NULL,
  `maxUsers` int(11) NOT NULL,
  `publicRoom` tinyint(4) NOT NULL,
  `moderated` tinyint(4) NOT NULL,
  `membersOnly` tinyint(4) NOT NULL,
  `canInvite` tinyint(4) NOT NULL,
  `roomPassword` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `canDiscoverJID` tinyint(4) NOT NULL,
  `logEnabled` tinyint(4) NOT NULL,
  `subject` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `rolesToBroadcast` tinyint(4) NOT NULL,
  `useReservedNick` tinyint(4) NOT NULL,
  `canChangeNick` tinyint(4) NOT NULL,
  `canRegister` tinyint(4) NOT NULL,
  PRIMARY KEY (`serviceID`,`name`),
  KEY `ofMucRoom_roomid_idx` (`roomID`),
  KEY `ofMucRoom_serviceid_idx` (`serviceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofMucRoom`
-- ----------------------------
BEGIN;
INSERT INTO `ofMucRoom` VALUES ('1', '2', '001418795779078', '001418800787105', '222', '222', '222', '000000000000000', '001419230576039', '1', '50', '1', '0', '0', '1', null, '0', '0', '222topic', '7', '0', '1', '1'), ('1', '1', '001418795656450', '001418795656520', 'asf', '111', '111', '000000000000000', '001418831854672', '0', '50', '1', '0', '0', '0', null, '0', '0', '111topic', '7', '0', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `ofMucRoomProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucRoomProp`;
CREATE TABLE `ofMucRoomProp` (
  `roomID` bigint(20) NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `propValue` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`roomID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofMucService`
-- ----------------------------
DROP TABLE IF EXISTS `ofMucService`;
CREATE TABLE `ofMucService` (
  `serviceID` bigint(20) NOT NULL,
  `subdomain` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `isHidden` tinyint(4) NOT NULL,
  PRIMARY KEY (`subdomain`),
  KEY `ofMucService_serviceid_idx` (`serviceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `propValue` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`serviceID`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofOffline`
-- ----------------------------
DROP TABLE IF EXISTS `ofOffline`;
CREATE TABLE `ofOffline` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `messageID` bigint(20) NOT NULL,
  `creationDate` char(15) COLLATE utf8_bin NOT NULL,
  `messageSize` int(11) NOT NULL,
  `stanza` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`messageID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofPresence`
-- ----------------------------
DROP TABLE IF EXISTS `ofPresence`;
CREATE TABLE `ofPresence` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `offlinePresence` text COLLATE utf8_bin,
  `offlineDate` char(15) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofPresence`
-- ----------------------------
BEGIN;
INSERT INTO `ofPresence` VALUES ('admin', null, '001419783064932'), ('lihao', null, '001419233378898');
COMMIT;

-- ----------------------------
--  Table structure for `ofPrivacyList`
-- ----------------------------
DROP TABLE IF EXISTS `ofPrivacyList`;
CREATE TABLE `ofPrivacyList` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `isDefault` tinyint(4) NOT NULL,
  `list` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`name`),
  KEY `ofPrivacyList_default_idx` (`username`,`isDefault`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofPrivate`
-- ----------------------------
DROP TABLE IF EXISTS `ofPrivate`;
CREATE TABLE `ofPrivate` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `namespace` varchar(200) COLLATE utf8_bin NOT NULL,
  `privateData` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`name`,`namespace`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofPrivate`
-- ----------------------------
BEGIN;
INSERT INTO `ofPrivate` VALUES ('lihao', 'storage', 'storage:bookmarks', 0x3c73746f7261676520786d6c6e733d2273746f726167653a626f6f6b6d61726b73222f3e);
COMMIT;

-- ----------------------------
--  Table structure for `ofProperty`
-- ----------------------------
DROP TABLE IF EXISTS `ofProperty`;
CREATE TABLE `ofProperty` (
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `propValue` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofProperty`
-- ----------------------------
BEGIN;
INSERT INTO `ofProperty` VALUES ('cumulus.path.default', 0x2f55736572732f6c6968616f2f446f776e6c6f6164732f6f70656e666972655f7372632f7461726765742f6f70656e666972652f706c7567696e732f726564666972652f63756d756c75732f43756d756c75735365727665722e657865), ('passwordKey', 0x56364e3832494876793948366b4371), ('provider.admin.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e61646d696e2e44656661756c7441646d696e50726f7669646572), ('provider.auth.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e617574682e44656661756c744175746850726f7669646572), ('provider.group.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e67726f75702e44656661756c7447726f757050726f7669646572), ('provider.lockout.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e6c6f636b6f75742e44656661756c744c6f636b4f757450726f7669646572), ('provider.securityAudit.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e73656375726974792e44656661756c745365637572697479417564697450726f7669646572), ('provider.user.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e757365722e44656661756c745573657250726f7669646572), ('provider.vcard.className', 0x6f72672e6a697665736f6674776172652e6f70656e666972652e76636172642e44656661756c74564361726450726f7669646572), ('update.lastCheck', 0x31343139373739323631353038), ('xmpp.auth.anonymous', 0x74727565), ('xmpp.domain', 0x6c6968616f732d6d6163626f6f6b2d70726f2e6c6f63616c), ('xmpp.session.conflict-limit', 0x30), ('xmpp.socket.ssl.active', 0x74727565);
COMMIT;

-- ----------------------------
--  Table structure for `ofPubsubAffiliation`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubAffiliation`;
CREATE TABLE `ofPubsubAffiliation` (
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) COLLATE utf8_bin NOT NULL,
  `affiliation` varchar(10) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
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
  `publisherModel` varchar(15) COLLATE utf8_bin NOT NULL,
  `subscriptionEnabled` tinyint(4) NOT NULL,
  `accessModel` varchar(10) COLLATE utf8_bin NOT NULL,
  `language` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `replyPolicy` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `associationPolicy` varchar(15) COLLATE utf8_bin NOT NULL,
  `maxLeafNodes` int(11) NOT NULL,
  PRIMARY KEY (`serviceID`,`leaf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) COLLATE utf8_bin NOT NULL,
  `id` varchar(100) COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) COLLATE utf8_bin NOT NULL,
  `creationDate` char(15) COLLATE utf8_bin NOT NULL,
  `payload` mediumtext COLLATE utf8_bin,
  PRIMARY KEY (`serviceID`,`nodeID`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofPubsubNode`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubNode`;
CREATE TABLE `ofPubsubNode` (
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) COLLATE utf8_bin NOT NULL,
  `leaf` tinyint(4) NOT NULL,
  `creationDate` char(15) COLLATE utf8_bin NOT NULL,
  `modificationDate` char(15) COLLATE utf8_bin NOT NULL,
  `parent` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `deliverPayloads` tinyint(4) NOT NULL,
  `maxPayloadSize` int(11) DEFAULT NULL,
  `persistItems` tinyint(4) DEFAULT NULL,
  `maxItems` int(11) DEFAULT NULL,
  `notifyConfigChanges` tinyint(4) NOT NULL,
  `notifyDelete` tinyint(4) NOT NULL,
  `notifyRetract` tinyint(4) NOT NULL,
  `presenceBased` tinyint(4) NOT NULL,
  `sendItemSubscribe` tinyint(4) NOT NULL,
  `publisherModel` varchar(15) COLLATE utf8_bin NOT NULL,
  `subscriptionEnabled` tinyint(4) NOT NULL,
  `configSubscription` tinyint(4) NOT NULL,
  `accessModel` varchar(10) COLLATE utf8_bin NOT NULL,
  `payloadType` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `bodyXSLT` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `dataformXSLT` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8_bin NOT NULL,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `language` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `replyPolicy` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `associationPolicy` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `maxLeafNodes` int(11) DEFAULT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) COLLATE utf8_bin NOT NULL,
  `rosterGroup` varchar(100) COLLATE utf8_bin NOT NULL,
  KEY `ofPubsubNodeGroups_idx` (`serviceID`,`nodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofPubsubNodeJIDs`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubNodeJIDs`;
CREATE TABLE `ofPubsubNodeJIDs` (
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) COLLATE utf8_bin NOT NULL,
  `associationType` varchar(20) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`jid`(70))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofPubsubSubscription`
-- ----------------------------
DROP TABLE IF EXISTS `ofPubsubSubscription`;
CREATE TABLE `ofPubsubSubscription` (
  `serviceID` varchar(100) COLLATE utf8_bin NOT NULL,
  `nodeID` varchar(100) COLLATE utf8_bin NOT NULL,
  `id` varchar(100) COLLATE utf8_bin NOT NULL,
  `jid` varchar(255) COLLATE utf8_bin NOT NULL,
  `owner` varchar(255) COLLATE utf8_bin NOT NULL,
  `state` varchar(15) COLLATE utf8_bin NOT NULL,
  `deliver` tinyint(4) NOT NULL,
  `digest` tinyint(4) NOT NULL,
  `digest_frequency` int(11) NOT NULL,
  `expire` char(15) COLLATE utf8_bin DEFAULT NULL,
  `includeBody` tinyint(4) NOT NULL,
  `showValues` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `subscriptionType` varchar(10) COLLATE utf8_bin NOT NULL,
  `subscriptionDepth` tinyint(4) NOT NULL,
  `keyword` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`serviceID`,`nodeID`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofRemoteServerConf`
-- ----------------------------
DROP TABLE IF EXISTS `ofRemoteServerConf`;
CREATE TABLE `ofRemoteServerConf` (
  `xmppDomain` varchar(255) COLLATE utf8_bin NOT NULL,
  `remotePort` int(11) DEFAULT NULL,
  `permission` varchar(10) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`xmppDomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofRoster`
-- ----------------------------
DROP TABLE IF EXISTS `ofRoster`;
CREATE TABLE `ofRoster` (
  `rosterID` bigint(20) NOT NULL,
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `jid` varchar(1024) COLLATE utf8_bin NOT NULL,
  `sub` tinyint(4) NOT NULL,
  `ask` tinyint(4) NOT NULL,
  `recv` tinyint(4) NOT NULL,
  `nick` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`rosterID`),
  KEY `ofRoster_unameid_idx` (`username`),
  KEY `ofRoster_jid_idx` (`jid`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofRoster`
-- ----------------------------
BEGIN;
INSERT INTO `ofRoster` VALUES ('1', 'lihao', 'admin@lihaos-macbook-pro.local', '3', '-1', '-1', 'adminsssss'), ('2', 'admin', 'lihao@lihaos-macbook-pro.local', '3', '-1', '-1', 'lihao');
COMMIT;

-- ----------------------------
--  Table structure for `ofRosterGroups`
-- ----------------------------
DROP TABLE IF EXISTS `ofRosterGroups`;
CREATE TABLE `ofRosterGroups` (
  `rosterID` bigint(20) NOT NULL,
  `rank` tinyint(4) NOT NULL,
  `groupName` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`rosterID`,`rank`),
  KEY `ofRosterGroup_rosterid_idx` (`rosterID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `principal` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`principal`(200))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofSecurityAuditLog`
-- ----------------------------
DROP TABLE IF EXISTS `ofSecurityAuditLog`;
CREATE TABLE `ofSecurityAuditLog` (
  `msgID` bigint(20) NOT NULL,
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `entryStamp` bigint(20) NOT NULL,
  `summary` varchar(255) COLLATE utf8_bin NOT NULL,
  `node` varchar(255) COLLATE utf8_bin NOT NULL,
  `details` text COLLATE utf8_bin,
  PRIMARY KEY (`msgID`),
  KEY `ofSecurityAuditLog_tstamp_idx` (`entryStamp`),
  KEY `ofSecurityAuditLog_uname_idx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofSecurityAuditLog`
-- ----------------------------
BEGIN;
INSERT INTO `ofSecurityAuditLog` VALUES ('1', 'admin', '1418225385124', 'uploaded plugin helloword.jar', 'lihaos-MacBook-Pro.local', null), ('2', 'admin', '1418225762552', 'uploaded plugin helloword.jar', 'lihaos-MacBook-Pro.local', null), ('3', 'admin', '1418699660073', 'reloaded plugin helloword', 'lihaos-MacBook-Pro.local', null), ('4', 'admin', '1418705045366', 'created new group g2', 'lihaos-MacBook-Pro.local', 0x6465736372697074696f6e203d206b6c), ('5', 'admin', '1418795656639', 'created new MUC room asf', 'lihaos-MacBook-Pro.local', 0x7375626a656374203d20313131746f7069630a726f6f6d64657363203d203131310a726f6f6d6e616d65203d203131310a6d61787573657273203d203530), ('6', 'admin', '1418795779174', 'created new MUC room 222', 'lihaos-MacBook-Pro.local', 0x7375626a656374203d20323232746f7069630a726f6f6d64657363203d203232320a726f6f6d6e616d65203d203232320a6d61787573657273203d203330), ('7', 'admin', '1418795810936', 'destroyed MUC room l;;k;', 'lihaos-MacBook-Pro.local', 0x726561736f6e203d206e756c6c0a616c74206a6964203d206e756c6c), ('8', 'admin', '1418800604077', 'updated MUC room 222', 'lihaos-MacBook-Pro.local', 0x7375626a656374203d20323232746f7069630a726f6f6d64657363203d203232320a726f6f6d6e616d65203d203232320a6d61787573657273203d203530), ('9', 'admin', '1418800787110', 'updated MUC room 222', 'lihaos-MacBook-Pro.local', 0x7375626a656374203d20323232746f7069630a726f6f6d64657363203d203232320a726f6f6d6e616d65203d203232320a6d61787573657273203d203530);
COMMIT;

-- ----------------------------
--  Table structure for `ofUser`
-- ----------------------------
DROP TABLE IF EXISTS `ofUser`;
CREATE TABLE `ofUser` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `plainPassword` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `encryptedPassword` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `creationDate` char(15) COLLATE utf8_bin NOT NULL,
  `modificationDate` char(15) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`),
  KEY `ofUser_cDate_idx` (`creationDate`),
  KEY `username_idx` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofUser`
-- ----------------------------
BEGIN;
INSERT INTO `ofUser` VALUES ('admin', null, 'da789aec909d2335a281e71c6e2161efa8cce5953c73b987', 'Administrator', 'admin@example.com', '001418699635712', '0'), ('lihao', null, '9501563ebb648b364fee0f6f485bc3ad82392f4e8eb547b2', 'lihao222', 'ss@qq.com', '001418298391486', '0'), ('tomcat2', null, '51ece0908cd1df19156b3118ddcb6a8941b3888e1719cbef', 'lksdf', null, '001420173979291', '001420173979291');
COMMIT;

-- ----------------------------
--  Table structure for `ofUserFlag`
-- ----------------------------
DROP TABLE IF EXISTS `ofUserFlag`;
CREATE TABLE `ofUserFlag` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `startTime` char(15) COLLATE utf8_bin DEFAULT NULL,
  `endTime` char(15) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`username`,`name`),
  KEY `ofUserFlag_sTime_idx` (`startTime`),
  KEY `ofUserFlag_eTime_idx` (`endTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofUserProp`
-- ----------------------------
DROP TABLE IF EXISTS `ofUserProp`;
CREATE TABLE `ofUserProp` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `propValue` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofVCard`
-- ----------------------------
DROP TABLE IF EXISTS `ofVCard`;
CREATE TABLE `ofVCard` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `vcard` mediumtext COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `ofVersion`
-- ----------------------------
DROP TABLE IF EXISTS `ofVersion`;
CREATE TABLE `ofVersion` (
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `ofVersion`
-- ----------------------------
BEGIN;
INSERT INTO `ofVersion` VALUES ('openfire', '21');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
