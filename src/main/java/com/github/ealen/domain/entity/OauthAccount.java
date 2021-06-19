package com.github.ealen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author EalenXie create on 2020/11/24 14:45
 * 自定义认证中心账号表
 */
@Data
@TableName("oauth_account")
public class OauthAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客户端id
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 账号名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 是否可用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 账号未过期
     */
    @TableField("account_non_expired")
    private Boolean accountNonExpired;

    /**
     * 账号未锁定
     */
    @TableField("account_non_locked")
    private Boolean accountNonLocked;

    /**
     * 密码未过期
     */
    @TableField("credentials_non_expired")
    private Boolean credentialsNonExpired;

    /**
     * 账号未删除(逻辑删除)
     */
    @TableField("account_non_deleted")
    private Boolean accountNonDeleted;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

}
