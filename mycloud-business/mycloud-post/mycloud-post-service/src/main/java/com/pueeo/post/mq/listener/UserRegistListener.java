package com.pueeo.post.mq.listener;

import com.alibaba.fastjson.JSONObject;
import com.pueeo.common.utils.SnowflakeIdUtil;
import com.pueeo.post.constant.PostConst;
import com.pueeo.post.entity.PostInfo;
import com.pueeo.post.service.PostInfoService;
import com.pueeo.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * 用户注册MQ消费监听
 *
 */
@Component
@Slf4j
public class UserRegistListener {

    @Autowired
    private PostInfoService postInfoService;

    /**
     * 用户成功注册后自动发送新人报道帖子
     */
    @Bean
    public Function<Flux<Message<UserProfile>>, Mono<Void>> userRegist() {
        return flux-> flux.map(message-> {
            log.info("Receive New Message: payload=【{}】, headers=【{}】", JSONObject.toJSONString(message.getPayload()), message.getHeaders());

            UserProfile userProfile = message.getPayload();
            PostInfo postInfo = new PostInfo();
            postInfo.setId(SnowflakeIdUtil.nextId());
            postInfo.setContent("新人报道");
            postInfo.setUid(userProfile.getUid());
            postInfo.setPostType(PostConst.PostType.image);
            postInfo.setPostStatus(PostConst.PostStatus.pass);
            postInfo.setImageUrl(userProfile.getAvatar());
            postInfoService.save(postInfo);
            return message;
        }).then();
    }

}
