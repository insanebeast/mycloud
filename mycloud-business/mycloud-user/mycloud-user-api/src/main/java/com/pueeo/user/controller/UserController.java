package com.pueeo.user.controller;

import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.sysconf.SysConfigEnum;
import com.pueeo.common.support.sysconf.SysConfigService;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.service.UserService;
import net.oschina.j2cache.CacheChannel;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StreamBridge streamBridge;
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 用户注册
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ApiResult register(@Validated UserRegisterDTO userDTO){
        return ApiResult.success(userService.register(userDTO));
    }

    @GetMapping("/keys")
    public ApiResult keys(){
        Collection<String> keys = cacheChannel.keys("USER");
        return ApiResult.success(keys);
    }

    /**
     * 查询用户
     *
     * @param uid
     * @return
     */
    @GetMapping("/get")
    public ApiResult get(@NotNull(message = "ID不能为空") Long uid){
        return ApiResult.success(userService.get(uid));
    }

    @GetMapping("/test")
    public ApiResult get(@Validated @RequestBody UserRegisterDTO userDTO){
        return ApiResult.success();
    }

    @PostMapping("/send")
    public String send(){
        Message<Long> message = MessageBuilder.withPayload(1001L)
                .setHeader(MessageConst.PROPERTY_TAGS, "abcx")
                .build();
        streamBridge.send("sms-out-0", message);
        return "ok";
    }

    public static void main(String[] args) {
        int num = 0;
        for (int i = 0; i < 100; i++) {
            num = num++;
        }
        System.out.println(num);
    }
}
