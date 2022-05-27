package com.pueeo.common.support.redis.lock;

import lombok.Getter;

@Getter
public enum RedisLockKey {

    //一个资源的锁
    TEST_ONE_PARAM_LOCK("lock:test:one:%s"),
    //多个资源的锁
    TEST_MORE_PARAM_LOCK("lock:test:more:%s_%s"),

    ;

    private String prefix;

    RedisLockKey(String prefix) {
        this.prefix = prefix;
    }

    public String getKey(Object... param) {
        return String.format(prefix, param);
    }
}
