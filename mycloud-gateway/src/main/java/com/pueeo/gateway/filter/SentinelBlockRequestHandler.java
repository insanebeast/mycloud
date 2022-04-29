package com.pueeo.gateway.filter;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.fastjson.JSONObject;
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

    private static final JSONObject res = new JSONObject();
    static {
        res.put("code", 429);
        res.put("msg", "blocked by sentinel");
        res.put("timestamp", System.currentTimeMillis());
    }

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS) // 状态码
                .contentType(MediaType.APPLICATION_JSON) // 内容类型
                .bodyValue(JSONObject.toJSONString(res)); // 错误提示
    }
}