package com.pueeo.gateway.exception;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.ResultEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义网关限流处理
 */
@Component
public class SentinelBlockRequestHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS) // 状态码
                .contentType(MediaType.APPLICATION_JSON) // 内容类型
                .bodyValue(ApiResult.fail(ResultEnum.FREQUENT_REQUEST)); // 错误提示
    }
}