package com.pueeo.common.support.redis.lock;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lockable {

    /**
     * 锁前缀
     */
    RedisLockKey prefix();

    /**
     * 加锁的资源，格式为SpEL表达式
     */
    String resource();

    /**
     * 锁超时时间（毫秒）
     */
    long expireTime() default 10000L;

    /**
     * 锁等待时间（毫秒）
     */
    long waitTime() default 0L;
}
