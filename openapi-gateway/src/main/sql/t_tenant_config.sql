CREATE TABLE t_tenant_config
(
    `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
    `tenant_id`   varchar(32)   NOT NULL DEFAULT '' COMMENT '租户id',
    `tenant_name` varchar(128)  NOT NULL DEFAULT '' COMMENT '租户名称',
    `tenant_desc` varchar(1024) NOT NULL DEFAULT '' COMMENT '租户简介',
    `app_key`     varchar(32)   NOT NULL DEFAULT '' COMMENT 'app_key',
    `secret`      char(32)      NOT NULL DEFAULT '' COMMENT 'secret',
    `public_key`  varchar(2048) NOT NULL DEFAULT '' COMMENT '公钥',
    `private_key` varchar(2048) NOT NULL DEFAULT '' COMMENT '私钥',
    `status` varchar(32) NOT NULL DEFAULT '' COMMENT '状态',
    `expire_time` bigint(20) unsigned DEFAULT NULL COMMENT '过期时间',
    `create_time` bigint(20) unsigned DEFAULT NULL COMMENT '创建时间',
    `update_time` bigint(20) unsigned DEFAULT NULL COMMENT '更新时间',

    primary key (`id`),
    UNIQUE KEY `uk_tenant_id` (`tenant_id`),
    UNIQUE KEY `uk_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
