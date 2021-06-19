SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

/* OAUTH2.0 系统表 */
drop table if exists oauth_access_token;

drop table if exists oauth_approvals;

drop table if exists oauth_client_details;

drop table if exists oauth_client_token;

drop table if exists oauth_code;

drop table if exists oauth_refresh_token;

/*==============================================================*/
/* Table: oauth_access_token                                    */
/*==============================================================*/
create table oauth_access_token
(
    token_id          varchar(255),
    token             blob,
    authentication_id varchar(255) not null,
    user_name         varchar(255),
    client_id         varchar(255),
    authentication    blob,
    refresh_token     varchar(255),
    primary key (authentication_id)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: oauth_approvals                                       */
/*==============================================================*/
create table oauth_approvals
(
    userId         varchar(255),
    clientId       varchar(255),
    scope          varchar(255),
    status         varchar(10),
    expiresAt      TIMESTAMP,
    lastModifiedAt TIMESTAMP
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: oauth_client_details                                  */
/*==============================================================*/
create table oauth_client_details
(
    client_id               varchar(255) not null,
    resource_ids            varchar(255),
    client_secret           varchar(255),
    scope                   varchar(255),
    authorized_grant_types  varchar(255),
    web_server_redirect_uri varchar(255),
    authorities             varchar(255),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  varchar(4096),
    autoapprove             varchar(255),
    primary key (client_id)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: oauth_client_token                                    */
/*==============================================================*/
create table oauth_client_token
(
    token_id          varchar(255),
    token             blob,
    authentication_id varchar(255) not null,
    user_name         varchar(255),
    client_id         varchar(255),
    primary key (authentication_id)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: oauth_code                                            */
/*==============================================================*/
create table oauth_code
(
    code           varchar(255),
    authentication blob
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: oauth_refresh_token                                   */
/*==============================================================*/
create table oauth_refresh_token
(
    token_id       varchar(255),
    token          blob,
    authentication blob
) ENGINE = InnoDB;


-- 自定义认证中心账号表
drop table if exists oauth_account;

/*==============================================================*/
/* Table: oauth_account                                         */
/*==============================================================*/
create table oauth_account
(
    id                      int(11)     not null auto_increment comment '账号ID',
    client_id               varchar(50) not null comment '客户端ID',
    username                varchar(50) not null comment '用户名',
    password                varchar(200) comment '密码',
    mobile                  varchar(13) comment '手机号',
    email                   varchar(100) comment '邮箱',
    enabled                 tinyint(1) comment '账号可用',
    account_non_expired     tinyint(1) default 1 comment '账号未过期',
    credentials_non_expired tinyint(1) default 1 comment '密码未过期',
    account_non_locked      tinyint(1) default 1 comment '账号未锁定',
    account_non_deleted     tinyint(1) default 1 comment '账号未删除',
    created_time            datetime   default CURRENT_TIMESTAMP comment '创建时间',
    updated_time            datetime   default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    primary key (id)
);

alter table `oauth_account`
    add index `user_idx` (`client_id`, `username`, `password`) using btree;
alter table oauth_account
    comment '自定义认证中心账号表';

