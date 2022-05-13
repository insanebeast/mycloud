package com.pueeo.user.controller;

import com.pueeo.common.support.ApiResult;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.service.UserAccountService;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/account")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private StreamBridge streamBridge;

    /**
     * 用户注册
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ApiResult register(UserRegisterDTO userDTO){
        return ApiResult.success(userAccountService.register(userDTO));
    }

    @PostMapping("/send")
    public String send(){
        Message<Long> message = MessageBuilder.withPayload(1001L)
                .setHeader(MessageConst.PROPERTY_TAGS, "abcx")
                .build();
        streamBridge.send("sms-out-0", message);
        return "ok";
    }
}
