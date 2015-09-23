CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'userid自增序列',
  `mobile` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '移动手机号',
  `passwd` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述信息',
  `avatar` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `birthday` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '生日',
  `token` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '注册后的apikey',
  `createtime` datetime NOT NULL COMMENT '创建日期',
  `lastmodifytime` datetime NOT NULL,
  `sex` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0 无 ，1为男性，2为女性',
  `province` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `city` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `country` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `source` tinyint(1) NOT NULL DEFAULT '1' COMMENT '来源网站，1代表weixin，2代表微博',
  PRIMARY KEY (`uid`),
  KEY `idx_user_mobile` (`mobile`) USING BTREE,
  KEY `idx_user_token` (`token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `boss_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'userid自增序列',
  `passwd` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'username',
  `nickname` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述信息',
  `avatar` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '用户类型， 1 是普通用户 ，2 是管理员',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮件地址',
  `createtime` datetime NOT NULL COMMENT '创建日期',
  `lastmodifytime` datetime NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `idx_user_name` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `boss_user` (`passwd`, `username`, `nickname`,description, avatar,type,email,createtime, `lastmodifytime`)
VALUES (password('111'), 'admin','管理员','管理员','',2,'admin', now(), now());
