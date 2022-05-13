package com.pueeo.user.mq;

import com.alibaba.fastjson.JSONObject;
import com.pueeo.common.constant.MQOutBinding;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.UUID;

@Service
@Slf4j
public class RocketMQMessageService {

    @Autowired
    private StreamBridge streamBridge;

    public String send(Object obj, MQOutBinding binding){
        String messageId = UUID.randomUUID().toString();
        Message<String> message = MessageBuilder.withPayload(JSONObject.toJSONString(obj))
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader(MessageConst.PROPERTY_TAGS, binding.getTag())
                .setHeader(MessageConst.PROPERTY_KEYS, messageId)
                .build();
        streamBridge.send(binding.getBinding(), message);
        return messageId;
    }
}
