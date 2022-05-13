package com.pueeo.common.support;

public enum RedisKey {

    SMS_CODE_LOGIN("sms_code:login:%s", "登录验证码"),

    ;

    private String key;
    private String desc;

    RedisKey(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey(Object... param) {
        return String.format(key, param);
    }
}
