-- MySQL dump 10.13  Distrib 8.0.33, for Linux (x86_64)
--
-- Host: localhost    Database: mas
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `mas_item`
--

DROP TABLE IF EXISTS `mas_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mas_item` (
  `uuid` varchar(100) NOT NULL DEFAULT '' COMMENT 'uuid',
  `name` varchar(100) NOT NULL DEFAULT '',
  `value` decimal(10,2) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `index_url` varchar(1000) DEFAULT NULL COMMENT 'URL',
  `description` text,
  `remain` decimal(10,0) DEFAULT '0',
  `expire_at` datetime DEFAULT NULL,
  `on_sale` tinyint(1) DEFAULT '0',
  `sort_index` bigint DEFAULT '1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` int DEFAULT '1' COMMENT '1, ; 2',
  `sold_count` int DEFAULT '0' COMMENT 'sold count',
  `valid_days` int DEFAULT '0',
  `usage_manual` text,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mas_item`
--

LOCK TABLES `mas_item` WRITE;
/*!40000 ALTER TABLE `mas_item` DISABLE KEYS */;
INSERT INTO `mas_item` VALUES ('1813500181265068033','500',500.00,0.01,'/static-resources/voucher500.png','',10,'2024-08-01 00:00:00',1,1,'2024-07-07 08:12:19','2024-08-14 08:31:38',1,0,0,NULL),('1823889445933346818','抖音小程序测试',10.00,9.00,'https://dkwl-plus.oss-cn-shenzhen.aliyuncs.com/dkwl-plus/tiktok/9_1723024920961.png','购买使用',50,NULL,1,0,'2024-08-15 03:48:26','2024-08-15 08:13:54',1,0,47,'购买使用'),('1823998333697454082','抖音小程序测试1',50.00,40.00,'https://dkwl-plus.oss-cn-shenzhen.aliyuncs.com/dkwl-plus/tiktok/9_1723024920961.png','测试',10,NULL,1,0,'2024-08-15 08:22:29','2024-08-15 08:22:29',2,0,20,'测试'),('1823998550404558849','抖音小程序测试2',15.00,10.00,'https://dkwl-plus.oss-cn-shenzhen.aliyuncs.com/dkwl-plus/tiktok/9_1723024920961.png','测试',100,NULL,1,0,'2024-08-15 08:22:33','2024-08-15 08:22:33',1,0,30,'测试');
/*!40000 ALTER TABLE `mas_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mas_order`
--

DROP TABLE IF EXISTS `mas_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mas_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) DEFAULT NULL,
  `item_id` varchar(100) NOT NULL COMMENT 'ID',
  `status` int NOT NULL DEFAULT '0',
  `user_id` bigint NOT NULL COMMENT 'ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `douyin_order_id` varchar(255) DEFAULT NULL,
  `douyin_order_token` varchar(255) DEFAULT NULL,
  `douyin_callback` text,
  `puchased_time` timestamp NULL DEFAULT NULL,
  `expire_time` timestamp NULL DEFAULT NULL,
  `puchased_no` varchar(255) DEFAULT NULL,
  `puchased_amount` int DEFAULT NULL,
  `notify_status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mas_order_mas_user_FK` (`user_id`),
  KEY `mas_order_mas_item_FK` (`item_id`),
  CONSTRAINT `mas_order_mas_item_FK` FOREIGN KEY (`item_id`) REFERENCES `mas_item` (`uuid`),
  CONSTRAINT `mas_order_mas_user_FK` FOREIGN KEY (`user_id`) REFERENCES `mas_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mas_order`
--

LOCK TABLES `mas_order` WRITE;
/*!40000 ALTER TABLE `mas_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `mas_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mas_param`
--

DROP TABLE IF EXISTS `mas_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mas_param` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pkey` varchar(100) DEFAULT NULL COMMENT 'KEY',
  `value` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mas_param`
--

LOCK TABLES `mas_param` WRITE;
/*!40000 ALTER TABLE `mas_param` DISABLE KEYS */;
INSERT INTO `mas_param` VALUES (1,'GUIDELINE','1、本券付款前使用。2、本券不兑换现金、不找零。3、使用本券消费时，不含税。4、使用本券消费时不另开发票。5、本券只限在注明日期内有效。');
/*!40000 ALTER TABLE `mas_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mas_picture`
--

DROP TABLE IF EXISTS `mas_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mas_picture` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pkey` varchar(100) NOT NULL,
  `url` varchar(500) NOT NULL COMMENT 'url',
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mas_picture_unique` (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mas_picture`
--

LOCK TABLES `mas_picture` WRITE;
/*!40000 ALTER TABLE `mas_picture` DISABLE KEYS */;
INSERT INTO `mas_picture` VALUES (2,'BANNER','/static-resources/banner.jpg','BANNER'),(3,'PRIVATE_PROTOCAL','/static-resources/private_protocal.pdf',NULL),(4,'USER_PROTOCAL','/static-resources/user_protocal.pdf',NULL);
/*!40000 ALTER TABLE `mas_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mas_user`
--

DROP TABLE IF EXISTS `mas_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mas_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(100) DEFAULT '',
  `openid` varchar(100) DEFAULT NULL COMMENT 'openid',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mas_user`
--

LOCK TABLES `mas_user` WRITE;
/*!40000 ALTER TABLE `mas_user` DISABLE KEYS */;
INSERT INTO `mas_user` VALUES (1,'user','1579246822','test-open-id','2024-07-08 12:07:09','2024-07-08 12:07:09'),(2,NULL,'','_000JdxaU2Gl_hG5YZDPYKlFzGLYxDQtBP0q','2024-08-07 12:51:18','2024-08-07 12:51:18'),(30,NULL,'18920363216','_000TEmB8K-UX8JbDGxkRrXmbFuWM5b9RzND','2024-08-13 10:27:57','2024-08-14 08:05:04');
/*!40000 ALTER TABLE `mas_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu_new`
--

DROP TABLE IF EXISTS `sys_menu_new`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu_new` (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` int DEFAULT NULL COMMENT '菜单父ID',
  `name` varchar(255) DEFAULT NULL COMMENT '路由名称',
  `path` varchar(255) DEFAULT NULL COMMENT '路由路径',
  `redirect` varchar(255) DEFAULT NULL COMMENT '路由重定向，有子集 children 时',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `title` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `is_link` varchar(255) DEFAULT NULL COMMENT '外链/内嵌时链接地址（http:xxx.com），开启外链条件，`1、isLink: 链接地址不为空`',
  `is_hide` int DEFAULT NULL COMMENT '是否隐藏',
  `is_keep_alive` int DEFAULT NULL COMMENT '是否缓存',
  `is_affix` int DEFAULT NULL COMMENT '是否固定',
  `is_iframe` int DEFAULT NULL COMMENT '是否内嵌，开启条件，`1、isIframe:true 2、isLink：链接地址不为空`',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `roles` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `order_sort` int DEFAULT NULL COMMENT '排序',
  `disabled` int DEFAULT NULL COMMENT '是否显示：1显示，0不显示',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu_new`
--

LOCK TABLES `sys_menu_new` WRITE;
/*!40000 ALTER TABLE `sys_menu_new` DISABLE KEYS */;
INSERT INTO `sys_menu_new` VALUES (1,0,'home','/home',NULL,'home/index','首页',NULL,0,1,1,0,'iconfont icon-shouye','admin,common',1,1),(2,0,'system','/system','/system/menu','layout/routerView/parent','系统管理',NULL,0,1,0,0,'iconfont icon-xitongshezhi','admin',2,1),(3,0,'logs','/logs',NULL,'layout/routerView/parent','日志管理',NULL,0,1,0,0,'el-icon-office-building','admin',3,1),(4,0,'codeGen','/codeGen',NULL,'code/index','代码生成',NULL,0,1,0,0,'el-icon-set-up','admin',98,1),(5,0,'swagger','/swagger',NULL,'layout/routerView/parent','接口文档','http://localhost:8080/swagger-ui/index.html',0,1,0,1,'el-icon-news','admin',99,1),(20,2,'systemUser','/systemUser',NULL,'user/index','用户管理',NULL,0,1,0,0,'el-icon-user','admin',1,1),(21,2,'systemRole','/systemRole',NULL,'role/index','角色管理',NULL,0,1,0,0,'el-icon-lock','admin',2,1),(22,2,'systemMenu','/systemMenu',NULL,'menu/index','菜单管理',NULL,0,1,0,0,'el-icon-box','admin',3,1),(23,2,'personal','/personal',NULL,'personal/index','个人中心',NULL,0,1,0,0,'iconfont icon-gerenzhongxin','admin,common',4,0),(31,3,'optionLog','/optionLog',NULL,'optionLog/index','操作日志',NULL,0,1,0,0,'el-icon-tickets','admin',1,1),(32,3,'loginLog','/loginLog',NULL,'loginLog/index','登录日志',NULL,0,1,0,0,'el-icon-tickets','admin',2,1),(38,2,'apkVersion','/apkVersion',NULL,'apk/index','APK版本管理',NULL,0,1,0,0,'iconfont icon-caidan','admin',99,1),(40,0,'masitem','/masitem',NULL,'masItem/index','商品管理',NULL,0,1,0,0,'el-icon-goods','admin',4,1),(41,0,'masuser','/masuser','','masUser/index','用户管理','',0,1,0,0,'el-icon-user','',0,1),(42,0,'masorder','/masorder','','masOrder/index','订单管理','',0,1,0,0,'el-icon-files','',0,1),(43,0,'maspicture','/maspicture',NULL,'masPicture/index','图片管理',NULL,0,1,0,0,'iconfont icon-caidan','admin',4,1),(44,0,'masparam','/masparam',NULL,'masParam/index','使用方法管理',NULL,0,1,0,0,'iconfont icon-caidan','admin',4,1);
/*!40000 ALTER TABLE `sys_menu_new` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss`
--

DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='文件上传';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss`
--

LOCK TABLES `sys_oss` WRITE;
/*!40000 ALTER TABLE `sys_oss` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oss` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (4,'管理员','管理员',0,'2019-04-18 10:12:05'),(5,'测试','测试',0,'2019-12-26 16:51:37');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (78,4,1),(79,4,2),(80,4,20),(81,4,21),(82,4,22),(83,4,38),(84,4,3),(85,4,31),(86,4,32),(87,4,4),(88,4,5),(89,4,40),(90,4,41),(91,4,42),(96,5,1),(97,5,40),(98,5,41),(99,5,42),(100,5,43);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `photo` varchar(500) DEFAULT NULL COMMENT '头像',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES ('0','adminmas','$2a$10$RG5KOoicH3f3IH948VW3AOzhJKepSteupeuQ8JAB28ElsYH3KlU4a','admin@qq.com','13612345678',1,'1','2016-11-11 11:11:11',NULL,NULL),('53e3215ed12b227b59b6b3b9e9efb984','superuser','$2a$10$oS5kglIxprGueKsbx1Gf7.Uk5AcI60YfaIJKv9Ejb9Nd2B0PRmONe','admin@qq.com','13888888888',1,'0','2023-01-29 14:31:16','/static-resources/icon.png',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (13,'53e3215ed12b227b59b6b3b9e9efb984',5);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_apk_version`
--

DROP TABLE IF EXISTS `tb_apk_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_apk_version` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `update_content` varchar(2000) DEFAULT NULL COMMENT '更新内容',
  `version_code` int DEFAULT NULL COMMENT '版本码',
  `version_name` varchar(20) DEFAULT NULL COMMENT '版本号',
  `package_name` varchar(255) DEFAULT NULL COMMENT '包名',
  `download_url` varchar(255) DEFAULT NULL COMMENT '下载地址',
  `app_name` varchar(255) DEFAULT NULL COMMENT 'APP名',
  `md5_value` varchar(255) DEFAULT NULL COMMENT 'MD5值',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_size` varchar(255) DEFAULT NULL COMMENT '文件大小',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `user_id` varchar(50) DEFAULT NULL COMMENT '上传人',
  `is_force` tinyint DEFAULT NULL COMMENT '是否强制安装',
  `is_ignorable` tinyint DEFAULT NULL COMMENT '是否可忽略该版本',
  `is_silent` tinyint DEFAULT NULL COMMENT '是否静默下载',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COMMENT='APK版本管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_apk_version`
--

LOCK TABLES `tb_apk_version` WRITE;
/*!40000 ALTER TABLE `tb_apk_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_apk_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_login_log`
--

DROP TABLE IF EXISTS `tb_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_login_log` (
  `log_id` varchar(50) NOT NULL COMMENT '登录日志ID',
  `username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `option_name` varchar(200) DEFAULT NULL COMMENT '操作',
  `option_time` datetime DEFAULT NULL COMMENT '操作时间',
  `option_ip` varchar(200) DEFAULT NULL,
  `option_terminal` text,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='登录日志管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_login_log`
--

LOCK TABLES `tb_login_log` WRITE;
/*!40000 ALTER TABLE `tb_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_login_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-16  8:50:25
