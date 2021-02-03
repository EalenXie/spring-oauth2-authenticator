package com.github.ealen.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author EalenXie create on 2020/11/24 19:16
 * 安全用户信息
 */
@Data
public class AccountInfo implements Serializable {

    private Long id;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 账号名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

}
