package com.pueeo.common.support.redis.lock;

import com.pueeo.common.support.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisLock {

    @Resource
    private RedisService redisService;

    private final long EXPIRE_TIME = 10000L; //默认10秒过期
    private final long WAIT_TIME = 0L; //默认不等待
    private final ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> String.format("%s-%s", Thread.currentThread().getName(), UUID.randomUUID()));

    private boolean lock(String lockKey, long expireTime, long waitTime){
        Assert.hasText(lockKey, "lockKey must not be empty");
        Assert.isTrue(expireTime >= 0, "expireTime cannot be negative");
        Assert.isTrue(waitTime >= 0, "waitTime cannot be negative");

        long start = System.currentTimeMillis();
        try{
            for(;;){
                boolean ret = redisService.setnx(lockKey, getLockValue(), expireTime);
                if(ret){
                    return true;
                }
                if (waitTime == 0){
                    return false;
                }
                //等待waitTime仍未获取到锁，则加锁失败
                long cost = System.currentTimeMillis() - start;
                if (cost >= waitTime) {
                    return false;
                }
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }catch (Exception e){
            log.error(String.format("fail to lock key [%s], caused by ", lockKey), e);
            unlock(lockKey);
            return false;
        }
    }

    private String getLockValue(){
        return threadLocal.get();
    }

    public boolean tryLock(String lockKey){
        return lock(lockKey, EXPIRE_TIME, WAIT_TIME);
    }

    public boolean tryLock(String lockKey, long waitTime){
        return lock(lockKey, EXPIRE_TIME, waitTime);
    }

    /**
     * 加锁
     *
     * @param lockKey key
     * @param expireTime 过期时间（毫秒）
     * @param waitTime 等待时间（毫秒）
     */
    public boolean tryLock(String lockKey, long expireTime, long waitTime){
        return lock(lockKey, expireTime, waitTime);
    }

    /**
     * 解锁
     *
     * @param lockKey
     * @return
     */
    public boolean unlock(String lockKey){
        Assert.hasText(lockKey, "lockKey must not be empty");

        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long ret = redisService.lua(luaScript, Long.class, Collections.singletonList(lockKey), getLockValue());
        return Objects.equals(1L, ret);
    }


}
