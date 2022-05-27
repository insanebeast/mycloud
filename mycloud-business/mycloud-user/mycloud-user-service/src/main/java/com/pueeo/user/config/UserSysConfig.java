package com.pueeo.user.config;

import lombok.Data;

/**
 * 用户系统配置
 */
@Data
public class UserSysConfig {

    /**
     * 强制实名认证开关
     */
    private boolean enableForceCertificate;
    /**
     * 强制绑定手机号
     */
    private boolean enableForceBindPhone;


}
