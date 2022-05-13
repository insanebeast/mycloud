package com.pueeo.common.constant;

/**
 * MQ生产者binding
 */
public enum MQOutBinding {

    /**
     * 用户注册消息
     */
    USER_REGIST("user-out-0", "userRegist"),


    ;


    MQOutBinding(String binding, String tag) {
        this.binding = binding;
        this.tag = tag;
    }

    private String binding;
    private String tag;

    public String getBinding() {
        return binding;
    }

    public String getTag() {
        return tag;
    }
}
