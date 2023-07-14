package com.morewen.gateway.security;

import com.google.gson.Gson;
import com.morewen.gateway.generator.domain.LoginUser;
import com.morewen.gateway.generator.domain.UserSubject;
import com.morewen.gateway.common.utils.JwtUtil;
import com.morewen.gateway.common.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    @Autowired
    private RedisCache redisCache;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractTokenFromRequest(exchange);
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                // 从redis中拿到用户的信息，给SecurityContextHolder设置上下文
                String sub = claims.getSubject();
                Gson gson = new Gson();
                UserSubject userSubject = gson.fromJson(sub, UserSubject.class);
                String username = userSubject.getUsername();
                LoginUser loginUser = redisCache.getCacheObject("login:" + username);
                if (Objects.isNull(loginUser)) {
                    throw new RuntimeException("用户未登录");
                }
                Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 将认证对象添加到请求的上下文中
                exchange.getAttributes().put("authentication", authentication);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
            } catch (Exception e) {
                return handleAuthenticationFailure(exchange);
            }
        } else {
            return handleAuthenticationFailure(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> handleAuthenticationFailure(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        throw new AccountExpiredException("身份检验失败！");
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
