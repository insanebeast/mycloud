package com.pueeo.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.ResultEnum;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    /**
     * 响应结果
     *
     * @param response
     * @param resultEnum
     * @throws Exception
     */
    public static void writeJson(HttpServletResponse response, ResultEnum resultEnum) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            out.write(objectMapper.writeValueAsString(ApiResult.fail(resultEnum)).getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应结果，基于reactive
     *
     * @param response
     * @param resultEnum
     * @return
     */
    public static Mono<Void> writeJson(ServerHttpResponse response, ResultEnum resultEnum) {
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body= JSONObject.toJSONString(ApiResult.fail(resultEnum));
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}