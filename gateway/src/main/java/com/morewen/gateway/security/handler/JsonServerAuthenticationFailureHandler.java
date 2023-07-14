package com.morewen.gateway.security.handler;

import com.morewen.gateway.common.constant.AjaxHttpStatus;
import com.morewen.gateway.common.utils.AjaxResult;
import com.morewen.gateway.security.handler.utils.HandlerResponseMaker;
import com.morewen.gateway.common.constant.AuthenticationErrorConstants;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class JsonServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    private static final Map<Class<? extends AuthenticationException>, String> ERROR_MESSAGES = new HashMap<>();
    static {
        ERROR_MESSAGES.put(UsernameNotFoundException.class, AuthenticationErrorConstants.USER_NOT_EXISTS);
        ERROR_MESSAGES.put(BadCredentialsException.class, AuthenticationErrorConstants.USERNAME_PASSWORD_ERROR);
        ERROR_MESSAGES.put(LockedException.class, AuthenticationErrorConstants.USER_LOCKED);
        ERROR_MESSAGES.put(AccountExpiredException.class, AuthenticationErrorConstants.TOKEN_VALIDATION_FAILED);
    }
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        String errorMessage = ERROR_MESSAGES.get(exception.getClass());
        if (Objects.isNull(errorMessage)) return writeErrorMessage(response, exception.getMessage());
        return writeErrorMessage(response, errorMessage);
    }


    private Mono<Void> writeErrorMessage(ServerHttpResponse response, String message) {

        response.setStatusCode(HttpStatus.valueOf(AjaxHttpStatus.HTTP_FORBIDDEN));
        AjaxResult result = AjaxResult.error(AjaxHttpStatus.HTTP_FORBIDDEN, message);
        DataBuffer buffer = HandlerResponseMaker.handlerMaker(response, result);
        // 返回响应结果
        return response.writeWith(Mono.just(buffer));
    }
}
