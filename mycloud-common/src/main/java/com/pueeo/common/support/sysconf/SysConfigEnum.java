package com.pueeo.common.support.sysconf;

import lombok.Getter;

/**
 * 系统配置枚举
 */
@Getter
public enum SysConfigEnum {
    /**
     * 帖子
     */
    POST("sysconf-post", "com.pueeo.post.config.PostSysConfig"),
    /**
     * 用户
     */
    USER("sysconf-user", "com.pueeo.user.config.UserSysConfig")
    ;

    /**
     *
     */
    private String key;

    /**
     * 全限定类名
     */
    private String clazz;

    SysConfigEnum(String key, String clazz) {
        this.key = key;
        this.clazz = clazz;
    }
}
