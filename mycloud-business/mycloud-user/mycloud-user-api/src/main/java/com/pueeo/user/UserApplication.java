package com.pueeo.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pueeo.user","com.pueeo.common"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    /**
     * 初始化一个MQ生产者
     * 不发送消息，解决Spring容器加载dubboProtocolConfigSupplier Bean时
     * 自动被Spring Cloud Stream注册为生产者频繁向MQ发送消息
     */
    @Bean
    public Supplier<Flux<String>> producer() {
        return Flux::empty;
    }
}
