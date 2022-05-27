package com.pueeo.post.config;

import lombok.Data;

/**
 * 帖子系统配置
 */
@Data
public class PostSysConfig {

    /**
     * 发帖开关
     */
    private boolean postSwitch;
    /**
     * 是否需要绑定手机号
     */
    private boolean isBindPhone;
    /**
     * 是否需要实名认证
     */
    private boolean isCertificate;


}
