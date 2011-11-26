/*
Navicat MySQL Data Transfer
Source Host     : localhost:3306
Source Database : exercise
Target Host     : localhost:3306
Target Database : exercise
Date: 2010-07-05 15:49:59
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_admin_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_authority`;
CREATE TABLE `t_admin_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_authority
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_role`;
CREATE TABLE `t_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_role_authority`;
CREATE TABLE `t_admin_role_authority` (
  `role_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  KEY `FKC5141495B1D1809C` (`role_id`),
  KEY `FKC51414954BA77178` (`authority_id`),
  CONSTRAINT `FKC51414954BA77178` FOREIGN KEY (`authority_id`) REFERENCES `t_admin_authority` (`id`),
  CONSTRAINT `FKC5141495B1D1809C` FOREIGN KEY (`role_id`) REFERENCES `t_admin_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_role_authority
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_sms
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_sms`;
CREATE TABLE `t_admin_sms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sms_key` varchar(255) DEFAULT NULL,
  `sms_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_sms
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_system_property
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_system_property`;
CREATE TABLE `t_admin_system_property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enable` bit(1) NOT NULL,
  `prop_describe` varchar(255) DEFAULT NULL,
  `prop_key` varchar(20) NOT NULL,
  `prop_name` varchar(20) NOT NULL,
  `prop_value` varchar(255) NOT NULL,
  `visible` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `prop_key` (`prop_key`),
  UNIQUE KEY `prop_name` (`prop_name`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_system_property
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_user`;
CREATE TABLE `t_admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_user_role`;
CREATE TABLE `t_admin_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKF44849AF56FC447C` (`user_id`),
  KEY `FKF44849AFB1D1809C` (`role_id`),
  CONSTRAINT `FKF44849AF56FC447C` FOREIGN KEY (`user_id`) REFERENCES `t_admin_user` (`id`),
  CONSTRAINT `FKF44849AFB1D1809C` FOREIGN KEY (`role_id`) REFERENCES `t_admin_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_admin_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_info
-- ----------------------------
DROP TABLE IF EXISTS `t_business_info`;
CREATE TABLE `t_business_info` (
  `ID` bigint(20) NOT NULL,
  `venue_id` bigint(20) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL COMMENT '工商注册名',
  `BANK` varchar(50) DEFAULT NULL COMMENT '开户银行',
  `BANK_ACCOUNT` varchar(20) DEFAULT NULL COMMENT '银行账号',
  `OWNER` varchar(10) DEFAULT NULL COMMENT '负责人',
  `OWNER_MOBILE` varchar(11) DEFAULT NULL COMMENT '负责人手机',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `venue_id` (`venue_id`),
  KEY `FKAE432D427FABFC72` (`venue_id`),
  CONSTRAINT `FKAE432D427FABFC72` FOREIGN KEY (`venue_id`) REFERENCES `t_venue_info` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_business_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_card_type
-- ----------------------------
DROP TABLE IF EXISTS `t_card_type`;
CREATE TABLE `t_card_type` (
  `id` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `type_name` varchar(50) DEFAULT NULL COMMENT '会员卡类型名称',
  `money_amount` bigint(20) DEFAULT NULL COMMENT '卡内金额',
  `period_month` int(11) DEFAULT NULL COMMENT '有效期长度',
  `discount_rate` bigint(20) DEFAULT NULL COMMENT '折扣率',
  `discount_time` varchar(10) DEFAULT NULL COMMENT '折扣范围，按照：非周末、周末按照顺序排列，只要对应位是1则包含此范围',
  `DISCOUNT_TYPE` varchar(2) DEFAULT NULL COMMENT '打折方式(折扣率、折扣额)',
  `DISCOUNT_PRICE` double DEFAULT NULL COMMENT '折扣单价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_card_type
-- ----------------------------

-- ----------------------------
-- Table structure for t_card_usage_record
-- ----------------------------
DROP TABLE IF EXISTS `t_card_usage_record`;
CREATE TABLE `t_card_usage_record` (
  `id` bigint(20) NOT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  `card_no` varchar(255) DEFAULT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `usage_date` datetime DEFAULT NULL,
  `usage_time_slice` varchar(50) DEFAULT NULL,
  `signature` varchar(50) DEFAULT NULL,
  `option_total` double DEFAULT NULL,
  `usage_type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_card_usage_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_badmintoon
-- ----------------------------
DROP TABLE IF EXISTS `t_field_badmintoon`;
CREATE TABLE `t_field_badmintoon` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `FIELD_CODE` varchar(255) DEFAULT NULL COMMENT '管理员自定义的编号',
  `NAME` varchar(50) DEFAULT NULL,
  `ENV_TYPE` varchar(20) DEFAULT NULL COMMENT '室内、室外、半露天、其他 环境类型不作为价格的参考，或者不不需要环境类型',
  `STATUS` varchar(2) DEFAULT NULL COMMENT '启用、维护、关闭',
  `ADVANCE` int(11) DEFAULT NULL COMMENT '提前预定天数',
  `ISSUE_ADVANCE` int(11) DEFAULT NULL COMMENT '可提前发布天数',
  `ISSUE_LAST_DATE` date DEFAULT NULL COMMENT '最后发布日期',
  PRIMARY KEY (`ID`),
  KEY `FK3FC488177FABFC72` (`VENUE_ID`),
  CONSTRAINT `FK3FC488177FABFC72` FOREIGN KEY (`VENUE_ID`) REFERENCES `t_venue_info` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_badmintoon
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_badmintoon_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_field_badmintoon_activity`;
CREATE TABLE `t_field_badmintoon_activity` (
  `ID` bigint(20) NOT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE` varchar(20) DEFAULT NULL,
  `FIELD_NAME` varchar(50) DEFAULT NULL,
  `PERIOD` varchar(50) DEFAULT NULL COMMENT '显示用，数据类型varchar就可以',
  `PRICE` int(11) DEFAULT NULL,
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` char(1) DEFAULT '0' COMMENT '1为验证，0为未验证 ',
  `ACTIVITY` varchar(50) DEFAULT NULL COMMENT '正在锻炼、过期、锻炼完毕',
  `FROM_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `TO_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `ORDER_USER` varchar(10) DEFAULT NULL COMMENT '预定会员姓名',
  `USABLE_DATE` date DEFAULT NULL COMMENT '可预订日期',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_ID` (`ORDER_ID`),
  KEY `FKD61EC63797E6DE58` (`FIELD_ID`),
  KEY `FKD61EC637CBFC7021` (`ORDER_ID`),
  CONSTRAINT `FKD61EC63797E6DE58` FOREIGN KEY (`FIELD_ID`) REFERENCES `t_field_badmintoon` (`ID`),
  CONSTRAINT `FKD61EC637CBFC7021` FOREIGN KEY (`ORDER_ID`) REFERENCES `t_field_order` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_badmintoon_activity
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_badmintoon_activity_tactics
-- ----------------------------
DROP TABLE IF EXISTS `t_field_badmintoon_activity_tactics`;
CREATE TABLE `t_field_badmintoon_activity_tactics` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE` varchar(20) DEFAULT NULL,
  `FIELD_NAME` varchar(50) DEFAULT NULL,
  `PERIOD` varchar(50) DEFAULT NULL COMMENT '显示用，数据类型varchar就可以',
  `PRICE` int(11) DEFAULT NULL,
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` varchar(1) DEFAULT NULL,
  `ACTIVITY` varchar(50) DEFAULT NULL COMMENT '正在锻炼、过期、锻炼完毕',
  `FROM_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `TO_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `ORDER_USER` varchar(10) DEFAULT NULL COMMENT '预定会员姓名',
  `USABLE_DATE` date DEFAULT NULL COMMENT '可预订日期',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_ID` (`ORDER_ID`),
  KEY `FKD0BE9C33CBFC7021` (`ORDER_ID`),
  CONSTRAINT `FKD0BE9C33CBFC7021` FOREIGN KEY (`ORDER_ID`) REFERENCES `t_field_order` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_badmintoon_activity_tactics
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_badmintoon_basic_price
-- ----------------------------
DROP TABLE IF EXISTS `t_field_badmintoon_basic_price`;
CREATE TABLE `t_field_badmintoon_basic_price` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `LOWEST_TIME` int(11) DEFAULT NULL COMMENT '数字表示',
  `LOWEST_TIME_TYPE` varchar(4) DEFAULT NULL COMMENT '1:分钟、2:小时',
  `PRICE` int(11) DEFAULT NULL,
  `FROM_TIME` varchar(5) DEFAULT NULL,
  `TO_TIME` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_badmintoon_basic_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_badmintoon_weekend_price
-- ----------------------------
DROP TABLE IF EXISTS `t_field_badmintoon_weekend_price`;
CREATE TABLE `t_field_badmintoon_weekend_price` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `LOWEST_TIME` int(11) DEFAULT NULL COMMENT '数字表示',
  `LOWEST_TIME_TYPE` varchar(4) DEFAULT NULL COMMENT '1:分钟、2:小时',
  `PRICE` int(11) DEFAULT NULL,
  `FROM_TIME` varchar(5) DEFAULT NULL,
  `TO_TIME` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_badmintoon_weekend_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_football
-- ----------------------------
DROP TABLE IF EXISTS `t_field_football`;
CREATE TABLE `t_field_football` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `FIELD_CODE` varchar(255) DEFAULT NULL COMMENT '管理员自定义的编号',
  `NAME` varchar(50) DEFAULT NULL,
  `ENV_TYPE` varchar(20) DEFAULT NULL COMMENT '室内、室外、半露天、其他 环境类型不作为价格的参考，或者不不需要环境类型',
  `STATUS` varchar(2) DEFAULT NULL COMMENT '启用、维护、关闭',
  `ADVANCE` int(11) DEFAULT NULL COMMENT '提前预定天数',
  `ISSUE_ADVANCE` int(11) DEFAULT NULL COMMENT '可提前发布天数',
  `ISSUE_LAST_DATE` date DEFAULT NULL COMMENT '最后发布日期',
  PRIMARY KEY (`ID`),
  KEY `FKC3CE2C3D7FABFC72` (`VENUE_ID`),
  CONSTRAINT `FKC3CE2C3D7FABFC72` FOREIGN KEY (`VENUE_ID`) REFERENCES `t_venue_info` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_football
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_football_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_field_football_activity`;
CREATE TABLE `t_field_football_activity` (
  `ID` bigint(20) NOT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE` varchar(20) DEFAULT NULL,
  `FIELD_NAME` varchar(50) DEFAULT NULL,
  `PERIOD` varchar(50) DEFAULT NULL COMMENT '显示用，数据类型varchar就可以',
  `PRICE` int(11) DEFAULT NULL,
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` char(1) DEFAULT '0' COMMENT '1为验证，0为未验证 ',
  `ACTIVITY` varchar(50) DEFAULT NULL COMMENT '正在锻炼、过期、锻炼完毕',
  `FROM_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `TO_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `ORDER_USER` varchar(10) DEFAULT NULL COMMENT '预定会员姓名',
  `USABLE_DATE` date DEFAULT NULL COMMENT '可预订日期',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_ID` (`ORDER_ID`),
  KEY `FK1B946CD14F8E887` (`FIELD_ID`),
  KEY `FK1B946CD1CBFC7021` (`ORDER_ID`),
  CONSTRAINT `FK1B946CD14F8E887` FOREIGN KEY (`FIELD_ID`) REFERENCES `t_field_football` (`ID`),
  CONSTRAINT `FK1B946CD1CBFC7021` FOREIGN KEY (`ORDER_ID`) REFERENCES `t_field_order` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_football_activity
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_football_activity_tactics
-- ----------------------------
DROP TABLE IF EXISTS `t_field_football_activity_tactics`;
CREATE TABLE `t_field_football_activity_tactics` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE` varchar(20) DEFAULT NULL,
  `FIELD_NAME` varchar(50) DEFAULT NULL,
  `PERIOD` varchar(50) DEFAULT NULL COMMENT '显示用，数据类型varchar就可以',
  `PRICE` int(11) DEFAULT NULL,
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` varchar(1) DEFAULT NULL,
  `ACTIVITY` varchar(50) DEFAULT NULL COMMENT '正在锻炼、过期、锻炼完毕',
  `FROM_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `TO_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `ORDER_USER` varchar(10) DEFAULT NULL COMMENT '预定会员姓名',
  `USABLE_DATE` date DEFAULT NULL COMMENT '可预订日期',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_ID` (`ORDER_ID`),
  KEY `FK625908CDCBFC7021` (`ORDER_ID`),
  CONSTRAINT `FK625908CDCBFC7021` FOREIGN KEY (`ORDER_ID`) REFERENCES `t_field_order` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_football_activity_tactics
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_football_basic_price
-- ----------------------------
DROP TABLE IF EXISTS `t_field_football_basic_price`;
CREATE TABLE `t_field_football_basic_price` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `LOWEST_TIME` int(11) DEFAULT NULL COMMENT '数字表示',
  `LOWEST_TIME_TYPE` varchar(4) DEFAULT NULL COMMENT '1:分钟、2:小时',
  `PRICE` int(11) DEFAULT NULL,
  `FROM_TIME` varchar(5) DEFAULT NULL,
  `TO_TIME` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_football_basic_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_football_weekend_price
-- ----------------------------
DROP TABLE IF EXISTS `t_field_football_weekend_price`;
CREATE TABLE `t_field_football_weekend_price` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `LOWEST_TIME` int(11) DEFAULT NULL COMMENT '数字表示',
  `LOWEST_TIME_TYPE` varchar(4) DEFAULT NULL COMMENT '1:分钟、2:小时',
  `PRICE` int(11) DEFAULT NULL,
  `FROM_TIME` varchar(5) DEFAULT NULL,
  `TO_TIME` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_football_weekend_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_order
-- ----------------------------
DROP TABLE IF EXISTS `t_field_order`;
CREATE TABLE `t_field_order` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `CARD_ID` bigint(20) DEFAULT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `CONTACT` varchar(10) DEFAULT NULL,
  `USER_CODE` varchar(10) DEFAULT NULL COMMENT '非会员默认为0',
  `PHONE` varchar(13) DEFAULT NULL COMMENT '会员、非会员都输入电话',
  `PAYMENT_STATUS` bit(1) DEFAULT NULL COMMENT '未付款、已付款',
  `BOOK_TIME` datetime DEFAULT NULL,
  `PAYMENT_TIME` datetime DEFAULT NULL,
  `PAYMENT_SUM` double DEFAULT NULL,
  `STANDARD_PRICE` double DEFAULT NULL,
  `PAYMENT_STYLE` varchar(10) DEFAULT NULL COMMENT '会员卡、现金、银行、支付宝、财付通',
  `PATCH` bit(1) DEFAULT NULL COMMENT '是否为补登计',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_tennis
-- ----------------------------
DROP TABLE IF EXISTS `t_field_tennis`;
CREATE TABLE `t_field_tennis` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `FIELD_CODE` varchar(255) DEFAULT NULL COMMENT '管理员自定义的编号',
  `NAME` varchar(50) DEFAULT NULL,
  `ENV_TYPE` varchar(20) DEFAULT NULL COMMENT '室内、室外、半露天、其他 环境类型不作为价格的参考，或者不不需要环境类型',
  `STATUS` varchar(2) DEFAULT NULL COMMENT '启用、维护、关闭',
  `ADVANCE` int(11) DEFAULT NULL COMMENT '提前预定天数',
  `ISSUE_ADVANCE` int(11) DEFAULT NULL COMMENT '可提前发布天数',
  `ISSUE_LAST_DATE` date DEFAULT NULL COMMENT '最后发布日期',
  PRIMARY KEY (`ID`),
  KEY `FKECA70DAB7FABFC72` (`VENUE_ID`),
  CONSTRAINT `FKECA70DAB7FABFC72` FOREIGN KEY (`VENUE_ID`) REFERENCES `t_venue_info` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_tennis
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_tennis_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_field_tennis_activity`;
CREATE TABLE `t_field_tennis_activity` (
  `ID` bigint(20) NOT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE` varchar(20) DEFAULT NULL,
  `FIELD_NAME` varchar(50) DEFAULT NULL,
  `PERIOD` varchar(50) DEFAULT NULL COMMENT '显示用，数据类型varchar就可以',
  `PRICE` int(11) DEFAULT NULL,
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` char(1) DEFAULT '0' COMMENT '1为验证，0为未验证 ',
  `ACTIVITY` varchar(50) DEFAULT NULL COMMENT '正在锻炼、过期、锻炼完毕',
  `FROM_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `TO_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `ORDER_USER` varchar(10) DEFAULT NULL COMMENT '预定会员姓名',
  `USABLE_DATE` date DEFAULT NULL COMMENT '可预订日期',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_ID` (`ORDER_ID`),
  KEY `FKE9B7472377C1A123` (`FIELD_ID`),
  KEY `FKE9B74723CBFC7021` (`ORDER_ID`),
  CONSTRAINT `FKE9B7472377C1A123` FOREIGN KEY (`FIELD_ID`) REFERENCES `t_field_tennis` (`ID`),
  CONSTRAINT `FKE9B74723CBFC7021` FOREIGN KEY (`ORDER_ID`) REFERENCES `t_field_order` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_tennis_activity
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_tennis_activity_tactics
-- ----------------------------
DROP TABLE IF EXISTS `t_field_tennis_activity_tactics`;
CREATE TABLE `t_field_tennis_activity_tactics` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `FIELD_ID` bigint(20) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL,
  `FIELD_TYPE` varchar(20) DEFAULT NULL,
  `FIELD_NAME` varchar(50) DEFAULT NULL,
  `PERIOD` varchar(50) DEFAULT NULL COMMENT '显示用，数据类型varchar就可以',
  `PRICE` int(11) DEFAULT NULL,
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` varchar(1) DEFAULT NULL,
  `ACTIVITY` varchar(50) DEFAULT NULL COMMENT '正在锻炼、过期、锻炼完毕',
  `FROM_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `TO_TIME` time DEFAULT NULL COMMENT '查询用，用datetime类型',
  `ORDER_USER` varchar(10) DEFAULT NULL COMMENT '预定会员姓名',
  `USABLE_DATE` date DEFAULT NULL COMMENT '可预订日期',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_ID` (`ORDER_ID`),
  KEY `FKB2ED711FCBFC7021` (`ORDER_ID`),
  CONSTRAINT `FKB2ED711FCBFC7021` FOREIGN KEY (`ORDER_ID`) REFERENCES `t_field_order` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_tennis_activity_tactics
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_tennis_basic_price
-- ----------------------------
DROP TABLE IF EXISTS `t_field_tennis_basic_price`;
CREATE TABLE `t_field_tennis_basic_price` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `LOWEST_TIME` int(11) DEFAULT NULL COMMENT '数字表示',
  `LOWEST_TIME_TYPE` varchar(4) DEFAULT NULL COMMENT '1:分钟、2:小时',
  `PRICE` int(11) DEFAULT NULL,
  `FROM_TIME` varchar(5) DEFAULT NULL,
  `TO_TIME` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_tennis_basic_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_tennis_weekend_price
-- ----------------------------
DROP TABLE IF EXISTS `t_field_tennis_weekend_price`;
CREATE TABLE `t_field_tennis_weekend_price` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL,
  `LOWEST_TIME` int(11) DEFAULT NULL COMMENT '数字表示',
  `LOWEST_TIME_TYPE` varchar(4) DEFAULT NULL COMMENT '1:分钟、2:小时',
  `PRICE` int(11) DEFAULT NULL,
  `FROM_TIME` varchar(5) DEFAULT NULL,
  `TO_TIME` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_tennis_weekend_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_field_type
-- ----------------------------
DROP TABLE IF EXISTS `t_field_type`;
CREATE TABLE `t_field_type` (
  `ID` bigint(20) NOT NULL,
  `TYPE_ID` varchar(20) DEFAULT NULL COMMENT '场地类型标示',
  `TYPE_NAME` varchar(20) DEFAULT NULL COMMENT '场地类型名称',
  `ENABLE` bit(1) DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TYPE_ID` (`TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_field_type
-- ----------------------------

-- ----------------------------
-- Table structure for t_information
-- ----------------------------
DROP TABLE IF EXISTS `t_information`;
CREATE TABLE `t_information` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `TITLE` varchar(200) DEFAULT NULL COMMENT '标题',
  `INFO_CONTENT` longtext COMMENT '内容',
  `CATEGORY` varchar(200) DEFAULT NULL COMMENT '分类',
  `INFO_LABEL` varchar(200) DEFAULT NULL COMMENT '标签',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建日期',
  `MODIFY_DATE` datetime DEFAULT NULL COMMENT '最后修改日期',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_information
-- ----------------------------

-- ----------------------------
-- Table structure for t_link_venue_fieldtype
-- ----------------------------
DROP TABLE IF EXISTS `t_link_venue_fieldtype`;
CREATE TABLE `t_link_venue_fieldtype` (
  `id` bigint(20) NOT NULL COMMENT '场馆和场地类型关联表主键',
  `field_type_id` bigint(20) NOT NULL COMMENT '地场类型ID',
  `venue_id` bigint(20) NOT NULL COMMENT '馆场ID',
  PRIMARY KEY (`id`),
  KEY `FKD069060A4BB320DC` (`field_type_id`),
  KEY `FKD069060A7FABFC72` (`venue_id`),
  CONSTRAINT `FKD069060A4BB320DC` FOREIGN KEY (`field_type_id`) REFERENCES `t_field_type` (`ID`),
  CONSTRAINT `FKD069060A7FABFC72` FOREIGN KEY (`venue_id`) REFERENCES `t_venue_info` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_link_venue_fieldtype
-- ----------------------------

-- ----------------------------
-- Table structure for t_member_card
-- ----------------------------
DROP TABLE IF EXISTS `t_member_card`;
CREATE TABLE `t_member_card` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `CARD_NUMBER` varchar(10) DEFAULT NULL COMMENT '会员卡号',
  `NAME` varchar(15) DEFAULT NULL COMMENT '会员姓名',
  `BALANCE` double DEFAULT NULL COMMENT '卡内余额',
  `MOBILE_PHONE` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `ID_NO` varchar(50) DEFAULT NULL COMMENT '身份证号码',
  `ADDRESS` varchar(200) DEFAULT NULL COMMENT '联系地址',
  `CARD_TYPE_ID` bigint(20) DEFAULT NULL,
  `PERIOD_VALIDITY` date DEFAULT NULL COMMENT '有效期',
  `EFFECT_DATE` date DEFAULT NULL COMMENT '生效日期',
  PRIMARY KEY (`ID`),
  KEY `FK40C28A4AB32BBFE6` (`CARD_TYPE_ID`),
  CONSTRAINT `FK40C28A4AB32BBFE6` FOREIGN KEY (`CARD_TYPE_ID`) REFERENCES `t_card_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_member_card
-- ----------------------------

-- ----------------------------
-- Table structure for t_tactics
-- ----------------------------
DROP TABLE IF EXISTS `t_tactics`;
CREATE TABLE `t_tactics` (
  `ID` bigint(20) NOT NULL,
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `FIELD_TYPE` varchar(20) DEFAULT NULL COMMENT '场地类型',
  `TACTICS_NAME` varchar(50) DEFAULT NULL COMMENT '策略名称',
  `IS_MODIFY` bit(1) DEFAULT NULL COMMENT '否是修改过日期、价格',
  `REMARK` longtext COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tactics
-- ----------------------------

-- ----------------------------
-- Table structure for t_tactics_date
-- ----------------------------
DROP TABLE IF EXISTS `t_tactics_date`;
CREATE TABLE `t_tactics_date` (
  `ID` bigint(20) NOT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL COMMENT '策略ID',
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `FROM_DATE` date DEFAULT NULL COMMENT '开始日期',
  `TO_DATE` date DEFAULT NULL COMMENT '结束日期',
  PRIMARY KEY (`ID`),
  KEY `FK43B9C51D3E794098` (`TACTICS_ID`),
  CONSTRAINT `FK43B9C51D3E794098` FOREIGN KEY (`TACTICS_ID`) REFERENCES `t_tactics` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tactics_date
-- ----------------------------

-- ----------------------------
-- Table structure for t_tactics_price
-- ----------------------------
DROP TABLE IF EXISTS `t_tactics_price`;
CREATE TABLE `t_tactics_price` (
  `ID` bigint(20) NOT NULL,
  `TACTICS_ID` bigint(20) DEFAULT NULL COMMENT '策略ID',
  `VENUE_ID` bigint(20) DEFAULT NULL COMMENT '场馆ID',
  `PRICE` int(11) DEFAULT NULL COMMENT '每单位收费',
  `FROM_TIME` varchar(5) DEFAULT NULL COMMENT '开始时段',
  `TO_TIME` varchar(5) DEFAULT NULL COMMENT '结束时段',
  PRIMARY KEY (`ID`),
  KEY `FK342F89BA3E794098` (`TACTICS_ID`),
  CONSTRAINT `FK342F89BA3E794098` FOREIGN KEY (`TACTICS_ID`) REFERENCES `t_tactics` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tactics_price
-- ----------------------------

-- ----------------------------
-- Table structure for t_venue_financial_statement
-- ----------------------------
DROP TABLE IF EXISTS `t_venue_financial_statement`;
CREATE TABLE `t_venue_financial_statement` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `venue_id` bigint(20) DEFAULT NULL,
  `t_date` datetime DEFAULT NULL,
  `amount_type` varchar(250) DEFAULT NULL COMMENT '现金：非现金：合计--现金：即时发生，现金：未来收入，现金：会员卡，现金：小计；非现金：以前预定，非现金：会员卡消费，非现金：小计；',
  `current_fields` int(11) DEFAULT NULL,
  `current_fees` bigint(20) DEFAULT NULL,
  `next_fields` int(11) DEFAULT NULL,
  `next_fees` bigint(20) DEFAULT NULL,
  `cash` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_venue_financial_statement
-- ----------------------------

-- ----------------------------
-- Table structure for t_venue_info
-- ----------------------------
DROP TABLE IF EXISTS `t_venue_info`;
CREATE TABLE `t_venue_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VENUE_NAME` varchar(50) DEFAULT NULL COMMENT '场馆名称',
  `PHONE` varchar(13) DEFAULT NULL COMMENT '联系电话',
  `FAX` varchar(13) DEFAULT NULL COMMENT '传真',
  `ADRESS` varchar(100) DEFAULT NULL COMMENT '地址',
  `CITY` varchar(20) DEFAULT NULL COMMENT '所在城市',
  `DISTRICT` varchar(20) DEFAULT NULL COMMENT '所在区(县)',
  `AREA` varchar(20) DEFAULT NULL COMMENT '商圈',
  `ZIPCODE` varchar(6) DEFAULT NULL COMMENT '邮编',
  `OPEN_TIME` varchar(5) DEFAULT NULL COMMENT '营业开始时间',
  `CLOSE_TIME` varchar(5) DEFAULT NULL COMMENT '营业结束时间',
  `CONTACT` varchar(10) DEFAULT NULL COMMENT '联系人',
  `CELL` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `EMAIL` varchar(30) DEFAULT NULL COMMENT '电子邮件',
  `INTRADUCTION` longtext COMMENT '场馆简介',
  `PHOTO_URL` longtext COMMENT '场馆图片位置',
  `AUTHENTICODE` varchar(10) DEFAULT NULL,
  `VERIFICATION` varchar(1) DEFAULT NULL,
  `send_sms` bit(1) DEFAULT NULL COMMENT '是否发短信',
  PRIMARY KEY (`ID`),
  KEY `INDEX_VENUE_INFO_VENUE-NAME` (`VENUE_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_venue_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_venue_member_info
-- ----------------------------
DROP TABLE IF EXISTS `t_venue_member_info`;
CREATE TABLE `t_venue_member_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '场地会员名',
  `VENUE_PASSWORD` varchar(50) DEFAULT NULL COMMENT '场地会员登录密码',
  `VENUE_REPASSWORD` varchar(50) DEFAULT NULL COMMENT '场地会员登录密码核对',
  `VENUE_NAME` varchar(50) DEFAULT NULL COMMENT '场地注册名称',
  `VENUE_ICNO` varchar(50) DEFAULT NULL COMMENT '场地工商号',
  `CITY` varchar(20) DEFAULT NULL COMMENT '所在地',
  `DISTRICT` varchar(20) DEFAULT NULL COMMENT '服务区域',
  `ADRESS` varchar(100) DEFAULT NULL COMMENT '场地地址',
  `PHONE1` varchar(20) DEFAULT NULL COMMENT '场地电话1',
  `PHONE2` varchar(20) DEFAULT NULL COMMENT '场地电话2',
  `FAX` varchar(20) DEFAULT NULL COMMENT '场地传真',
  `PEOPLE_NUM` varchar(40) DEFAULT NULL COMMENT '人制',
  `BUSSINESS_TIME` varchar(30) DEFAULT NULL COMMENT '营业时间',
  `TIMEI_PRICE1` varchar(40) DEFAULT NULL COMMENT '时段价格1',
  `TIMEI_PRICE2` varchar(40) DEFAULT NULL COMMENT '时段价格2',
  `TIMEI_PRICE3` varchar(40) DEFAULT NULL COMMENT '时段价格3',
  `TIMEI_PRICE4` varchar(40) DEFAULT NULL COMMENT '时段价格4',
  `MON_FRI` varchar(40) DEFAULT NULL COMMENT '周一至周五',
  `WEEKEND` varchar(40) DEFAULT NULL COMMENT '周末',
  `MEMBER_YN` char(1) DEFAULT 'N' COMMENT '有无会员',
  `MEMBER_PRICE` varchar(50) DEFAULT NULL COMMENT '会员价格',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_venue_member_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_venue_user
-- ----------------------------
DROP TABLE IF EXISTS `t_venue_user`;
CREATE TABLE `t_venue_user` (
  `ID` bigint(20) NOT NULL,
  `venue_id` bigint(20) DEFAULT NULL,
  `USERNAME` varchar(16) DEFAULT NULL COMMENT '场馆管理员名',
  `PASSWORD` varchar(16) DEFAULT NULL COMMENT '密码',
  `STATUS` varchar(2) DEFAULT NULL COMMENT '激活、暂停使用等',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `venue_id` (`venue_id`),
  KEY `FK57FCAC467FABFC72` (`venue_id`),
  CONSTRAINT `FK57FCAC467FABFC72` FOREIGN KEY (`venue_id`) REFERENCES `t_venue_info` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_venue_user
-- ----------------------------

-- ----------------------------
-- Procedure structure for build_financial_statement
-- ----------------------------
DROP PROCEDURE IF EXISTS `build_financial_statement`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `build_financial_statement`()
BEGIN
	DECLARE fetchSeqOk BOOLEAN; ## define the flag for loop judgement
	DECLARE _venue_name VARCHAR(250)  CHARACTER SET utf8;
	DECLARE insert_str VARCHAR(250)  CHARACTER SET utf8;
	DECLARE _venue_id BIGINT;
	
	DECLARE fetchSeqCursor CURSOR FOR 
		SELECT venue_name, id 
			FROM t_venue_info ; 
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk = TRUE;
	SET fetchSeqOk = FALSE; 
	OPEN fetchSeqCursor;
		WHILE NOT fetchSeqOk DO
			FETCH fetchSeqCursor INTO _venue_name, _venue_id;
			/*
		amount_type:
		即时发生的现金
		以后发生的现金
		会员卡销售的现金
		以前收到现金并当日锻炼实现的收入
		会员卡消费实现的收入
		小计*/	
			IF NOT EXISTS (SELECT * FROM t_venue_financial_statement WHERE t_date = CURRENT_DATE() AND venue_id = _venue_id) THEN
				SET insert_str = '结余';
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
			SET insert_str = '即时发生的现金';
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
SET insert_str = '以后发生的现金';			
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
SET insert_str = '会员卡销售的现金';			
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);			
			
SET insert_str = '现金小计';			
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
SET insert_str = '以前收到现金并当日锻炼实现的收入';			
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
SET insert_str = '会员卡消费实现的收入';			
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
SET insert_str = '非现金小计';			
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			
			SET insert_str = '合计及结余';
			INSERT INTO t_venue_financial_statement (venue_id,t_date,amount_type,current_fields,current_fees,next_fields,next_fees,cash) 
			VALUES(_venue_id,CURRENT_DATE(),insert_str,0,0,0,0,0);
			END IF;
			
			/*处理结算*/
			SET @yestoday_cash =0;
			SET @current_fee =0;
			SET @next_fee =0;
			IF EXISTS (SELECT @yestoday_cash:=cash FROM t_venue_financial_statement 
				WHERE ( TO_DAYS(CURRENT_DATE())-TO_DAYS(t_date))=1  
					AND venue_id = _venue_id 
					AND amount_type ='合计及结余') THEN
				UPDATE t_venue_financial_statement  
				SET cash = @yestody_cash
				WHERE t_date = CURRENT_DATE() AND venue_id = _venue_id AND amount_type ='结余'; 			
			END IF;
			SELECT @current_fee:=@current_fee+current_fees,@next_fee:=@next_fee+next_fees FROM t_venue_financial_statement 
				WHERE t_date=CURRENT_DATE() 
					AND venue_id = _venue_id 
					AND amount_type ='现金小计';
			SELECT @current_fee:=@current_fee+current_fees,@next_fee:=@next_fee+next_fees FROM t_venue_financial_statement 
				WHERE t_date=CURRENT_DATE() 
					AND venue_id = _venue_id 
					AND amount_type ='非现金小计';		
			UPDATE t_venue_financial_statement  
				SET cash = cash + @yestody_cash +@current_fee + @next_fee
			WHERE t_date = CURRENT_DATE() AND venue_id = _venue_id AND amount_type ='合计及结余';				
		END WHILE;
		
		
		
	CLOSE fetchSeqCursor; 
	
	SELECT * FROM t_venue_financial_statement WHERE t_date = CURRENT_DATE();
	
	
	
    END;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for count_financial_statement
-- ----------------------------
DROP PROCEDURE IF EXISTS `count_financial_statement`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `count_financial_statement`($venue_id INT)
BEGIN
    SET @count_table =CONCAT('UPDATE t_venue_financial_statement SET cash = current_fees + next_fees', 
		'  WHERE t_date = CURRENT_DATE() and venue_id =',$venue_id,';');
	PREPARE s1 FROM @count_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	SELECT * FROM t_venue_financial_statement WHERE t_date = CURRENT_DATE() AND VENUE_ID = $venue_id;
    END;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for report_daily
-- ----------------------------
DROP PROCEDURE IF EXISTS `report_daily`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `report_daily`($venue_id INT, $diff_date VARCHAR(250),$activity_status VARCHAR(250))
BEGIN
	DECLARE fetchSeqOk BOOLEAN; ## define the flag for loop judgement
	DECLARE _field_name VARCHAR(50);
	DECLARE _field_id BIGINT;
	DECLARE _cnt INT;
	DECLARE _count INT;	
	DECLARE _sumprice INT;
	DECLARE _str VARCHAR(1000);
	DECLARE sqlstr VARCHAR(1000);
	DECLARE _sql VARCHAR(1000);
	DECLARE fetchSeqCursor CURSOR FOR 
		SELECT DISTINCT FIELD_NAME, FIELD_ID 
			FROM t_field_badmintoon_activity 
				WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date
				ORDER BY FIELD_ID;   
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk = TRUE;
	
	SELECT COUNT(DISTINCT FIELD_ID) FROM t_field_badmintoon_activity 
				WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date
	INTO _cnt;
	SET $activity_status ='锻炼';	
	SET fetchSeqOk = FALSE; 	
	SET _count = 0;
	SET _str = "";
	
	SET @table_name = CONCAT('tmp_venueID',$venue_id,'_',YEAR($diff_date),'_',MONTH($diff_date),'_',DAY($diff_date));
	SET @drop_table = CONCAT('drop table if exists ',@table_name);
	PREPARE s1 FROM @drop_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	
	-- Check whether the table exists or not
	SET @create_table = CONCAT('create table if not exists ',
								@table_name,
								' (ID	bigint auto_increment not null,	period varchar(250), primary key (ID));');
	PREPARE s1 FROM @create_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	SET _sql =' ';
	SET @COLUMNS='';
	OPEN fetchSeqCursor;
		WHILE NOT fetchSeqOk DO
		-- BUILD TEMP TABLE add columns 
			
				FETCH fetchSeqCursor INTO _field_name, _field_id;				
				IF _cnt > 0 THEN
					SET @COLUMNS = CONCAT(@COLUMNS,' add `',_field_id,'` varchar(100) not null, ');
					SET _sql = CONCAT(_sql,',`',_field_id,'` ',_field_name);
					-- count 
					SELECT COUNT(*) FROM t_field_badmintoon_activity 
						WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date AND FIELD_ID LIKE _field_id AND ACTIVITY LIKE $activity_status
					INTO _count;
					SET _str = CONCAT(_str,',"',_count,'"');
					SET _count = 0;
				END IF;
				SET _cnt = _cnt -1;
			
		END WHILE;
		SET @COLUMNS= CONCAT(@COLUMNS,' add `status_total` int, add `sum_price` int, add `from_time` time; ');
		
		SET @stmt1 = CONCAT('alter table ',@table_name,@COLUMNS);
		-- select @stmt1;
			
		PREPARE s1 FROM @stmt1;
		EXECUTE s1;
		DEALLOCATE PREPARE s1; 
	
		-- insert total record
		SET _str = CONCAT(_str,',',0);
		SET _str = CONCAT(_str,',',0);
						
		SET @stmt1 = CONCAT('insert into ',@table_name,' values ("","',$activity_status,'_total"',_str,',"00-00-00")');
		
		-- select @stmt1;
			PREPARE s1 FROM @stmt1;
			EXECUTE s1;
			DEALLOCATE PREPARE s1;
	CLOSE fetchSeqCursor; 
	
	BEGIN -- 填充数据
		DECLARE fetchSeqOk2 BOOLEAN;
		-- declare _cnt int;
		DECLARE _period  VARCHAR(50);
		DECLARE _field_name  VARCHAR(250);
		DECLARE _field_id  BIGINT;
		DECLARE t_period  VARCHAR(50);
		DECLARE	_activity  VARCHAR(50);
		DECLARE str  VARCHAR(1000);
		DECLARE _from_time  TIME;
		DECLARE _price INT;
		DECLARE _payment_sum INT;
		DECLARE count_status INT;
		DECLARE total_price INT;
		DECLARE total_count INT;
		DECLARE t_time TIME;
		DECLARE fetchSeqCursor2 CURSOR FOR 
			SELECT FIELD_NAME, a.FIELD_ID, PERIOD, ACTIVITY, PRICE, PAYMENT_SUM, FROM_TIME
				FROM t_field_badmintoon_activity a
					LEFT JOIN t_field_order ON order_id = t_field_order.id
					WHERE 1  AND a.VENUE_ID = $venue_id AND USABLE_DATE = $diff_date					
					ORDER BY PERIOD,FIELD_id; 
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET fetchSeqOk2 = TRUE;
		
		SET fetchSeqOk2 = FALSE;
		
		SELECT DISTINCT COUNT(FIELD_ID)
			FROM t_field_badmintoon_activity 
				WHERE 1  AND VENUE_ID = $venue_id AND USABLE_DATE = $diff_date
		INTO _cnt; 
		
		-- init params
		SET t_period = '';
		SET t_time ='';
		SET str = '';
		SET _sumprice=0;
		SET count_status = 0;
		SET total_price =0;
		SET total_count =0;
		
		OPEN fetchSeqCursor2;
			WHILE NOT fetchSeqOk2 DO
			-- get item
				FETCH fetchSeqCursor2 INTO _field_name, _field_id, _period, _activity, _price,_payment_sum, _from_time;
				SET _cnt = _cnt -1;
				SET _price = IFNULL(_payment_sum,_price);
				IF t_period = '' AND NOT(_period IS NULL) THEN
					SET t_period = _period;
					SET t_time = _from_time;
				END IF;
				
				IF t_period <> _period OR _cnt <0 THEN
					SET str = CONCAT(str,',',count_status);
					SET str = CONCAT(str,',',_sumprice);
					
					SET @stmt2 = CONCAT('insert into ',@table_name,' values ("","',t_period,'"',str,',"',t_time,'"',')');
					
					SET total_price = total_price + _sumprice;
					SET total_count = total_count + count_status;
					SET str = '';
					SET count_status = 0;
					SET _sumprice = 0;
					
					PREPARE s2 FROM @stmt2;
					EXECUTE s2;
					DEALLOCATE PREPARE s2;
					
					SET t_period = _period;
					SET t_time = _from_time;
				END IF;					
					
				SET str = CONCAT(str,',"',CONVERT(_activity USING utf8),',',_price,'"');
				
				IF _activity  = $activity_status THEN
					-- 计算当前传入状态
					SET count_status = count_status + 1; 
					SET _sumprice = _sumprice + _price;
				END IF;
							
			END WHILE;
					
		CLOSE fetchSeqCursor2;
		-- update total
		SET @ss = CONCAT('update ',@table_name,' set status_total=',total_count,', sum_price=',total_price,' where id =1');
		PREPARE s1 FROM @ss;
		EXECUTE s1;
		DEALLOCATE PREPARE s1;
	
	END;
	
	SET @ss = CONCAT('select id,period',_sql,',status_total,sum_price from ',@table_name," order by from_time");
	PREPARE s1 FROM @ss;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
	
	PREPARE s1 FROM @drop_table;
	EXECUTE s1;
	DEALLOCATE PREPARE s1; 
END;;
DELIMITER ;
