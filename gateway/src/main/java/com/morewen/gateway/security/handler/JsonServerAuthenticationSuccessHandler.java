package com.morewen.gateway.security.handler;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morewen.gateway.common.utils.AjaxResult;
import com.morewen.gateway.generator.domain.LoginUser;
import com.morewen.gateway.generator.domain.SysUser;
import com.morewen.gateway.common.utils.JwtUtil;
import com.morewen.gateway.common.utils.RedisCache;
import com.morewen.gateway.security.handler.utils.HandlerResponseMaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JsonServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Autowired
    RedisCache redisCache;
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        // 登录成功后可以放入一些参数到session中
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        //拿到用户信息 然后生成jwt返回给前端，并且将用户的信息存入redis
        Object principal = authentication.getPrincipal();
        User user = (User) principal;
        Map<String, String> load = new HashMap<>();
        load.put("username",authentication.getName());
        List<? extends GrantedAuthority> list=authentication.getAuthorities().stream().collect(Collectors.toList());
        load.put("role", list.toString());
        log.info(authentication.toString());
        String tokenStr = load.toString();
        String jwt = JwtUtil.createJWT(tokenStr);
        SysUser sysUser = new SysUser(user.getUsername(), user.getPassword());
        List<String> stringList = list.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        LoginUser loginUser = new LoginUser(sysUser, stringList);
        redisCache.setCacheObject("login:"+authentication.getName(), loginUser);//将用户信息直接存入redis
        AjaxResult result = AjaxResult.success("登陆成功！", MapUtil.builder()
                .put("token", jwt)
                .build());
        // 构建数据缓冲区并写入响应
        DataBuffer buffer = HandlerResponseMaker.handlerMaker(response, result);
        return response.writeWith(Mono.just(buffer));
    }
}
