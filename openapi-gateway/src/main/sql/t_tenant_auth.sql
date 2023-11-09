CREATE TABLE t_tenant_auth
(
    `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
    `tenant_id`   varchar(32)   NOT NULL DEFAULT '' COMMENT '租户id',
    `resource_code` varchar(128) NOT NULL DEFAULT '' COMMENT '授权资源',
    `deleted` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否删除',
    `create_time` bigint(20) unsigned DEFAULT NULL COMMENT '创建时间',
    `update_time` bigint(20) unsigned DEFAULT NULL COMMENT '更新时间',

    primary key (`id`),
    UNIQUE KEY `uk_tenant_resource` (`tenant_id`, `resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
