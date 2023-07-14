package com.morewen.gateway.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.morewen.gateway.common.utils.AjaxResult;
import com.morewen.gateway.common.utils.JwtUtil;
import com.morewen.gateway.common.utils.RedisCache;
import com.morewen.gateway.generator.domain.UserSubject;
import com.morewen.gateway.security.handler.utils.HandlerResponseMaker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class JsonServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {
    @Autowired
    RedisCache redisCache;
    @SneakyThrows
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        String token = extractTokenFromRequest(exchange.getExchange());
        String jsonResult;
        // 将结果对象转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        ServerHttpResponse response = exchange.getExchange().getResponse();
        if (token == null) {
            throw new AccountExpiredException("未登录或者token无效");
        }else {
            try {
                String subject = JwtUtil.parseJWT(token).getSubject();
                Gson gson = new Gson();
                UserSubject userSubject = gson.fromJson(subject, UserSubject.class);
                // 获取 "username" 对应的值
                String username = userSubject.getUsername();
                String key = "login:" + username;
                if (!redisCache.deleteObject(key))throw new RuntimeException("redis缓存删除失败！");
                AjaxResult result = AjaxResult.success("注销成功");
                response.setStatusCode(HttpStatus.OK);
                // 构建数据缓冲区并写入响应
                DataBuffer buffer = HandlerResponseMaker.handlerMaker(response, result);
                return response.writeWith(Mono.just(buffer));
            } catch (Exception e) {
                log.error(null, e);
                throw new AccountExpiredException("未登录或者token无效");
            }
        }

    }
    private String extractTokenFromRequest(ServerWebExchange exchange) {
        // 从请求头或请求参数中提取token
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

}