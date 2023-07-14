package com.morewen.gateway.security.handler;

import com.morewen.gateway.common.constant.AjaxHttpStatus;
import com.morewen.gateway.common.utils.AjaxResult;
import com.morewen.gateway.security.handler.utils.HandlerResponseMaker;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException exception) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(AjaxHttpStatus.HTTP_FORBIDDEN));
        AjaxResult result = AjaxResult.error(AjaxHttpStatus.HTTP_FORBIDDEN, "当前用户没有访问权限!");
        DataBuffer buffer = HandlerResponseMaker.handlerMaker(response, result);
        // 返回响应结果
        return response.writeWith(Mono.just(buffer));
    }
}
