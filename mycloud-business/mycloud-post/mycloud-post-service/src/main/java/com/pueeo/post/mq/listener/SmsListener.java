package com.pueeo.post.mq.listener;

import com.pueeo.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Slf4j
public class SmsListener {

    /**
     * test
     */
//    @Bean
//    public Function<Flux<Message<String>>, Mono<Void>> welcomeSms() {
//        return flux-> flux.map(message-> {
//            log.info("Receive New Message: payload=【{}】, headers=【{}】", message.getPayload(), message.getHeaders());
//            //do send sms
//            log.info("欢迎【{}】加入快乐星球～！", message.getPayload().getNick());
//            return message;
//        }).then();
//    }

    @Bean
    public Consumer<Message<String>> sms() {
        return message ->  log.info("Receive New Message: payload=【{}】, headers=【{}】", message.getPayload(), message.getHeaders());
    }
}
