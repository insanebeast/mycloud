package com.pueeo.gateway;

import com.google.common.collect.Lists;
import com.pueeo.common.support.redis.RedisService;
import com.pueeo.common.constant.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitService {
    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void init(){
        redisService.hset(SysConstant.OAUTH_URLS,"/post/degrade", Lists.newArrayList("ROLE_admin","ROLE_user"));
        redisService.hset(SysConstant.OAUTH_URLS,"/post/add", Lists.newArrayList("ROLE_admin"));
    }

}