/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 50560
 Source Host           : localhost:3306
 Source Schema         : authorization_center

 Target Server Type    : MySQL
 Target Server Version : 50560
 File Encoding         : 65001

 Date: 02/02/2021 15:25:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_account
-- ----------------------------
DROP TABLE IF EXISTS `oauth_account`;
CREATE TABLE `oauth_account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `mobile` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否可用',
  `account_non_expired` tinyint(1) NULL DEFAULT NULL COMMENT '账号是否未过期',
  `credentials_non_expired` tinyint(1) NULL DEFAULT NULL COMMENT '密码是否未过期',
  `account_non_locked` tinyint(1) NULL DEFAULT NULL COMMENT '账号未被锁定',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '账号是否删除(逻辑删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `client_username_index`(`client_id`, `username`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '认证中心 账号表' ROW_FORMAT = Compact;

