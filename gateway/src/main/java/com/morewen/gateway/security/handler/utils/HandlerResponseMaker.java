package com.morewen.gateway.security.handler.utils;

import com.morewen.gateway.common.utils.AjaxResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.nio.charset.StandardCharsets;

public class HandlerResponseMaker {
    public static DataBuffer handlerMaker(ServerHttpResponse response, AjaxResult result) {
        String jsonResponse = AjaxResult.convertObjectToJson(result);
        // 设置响应头信息
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 将JSON字符串写入响应体
        return response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
    }
}
