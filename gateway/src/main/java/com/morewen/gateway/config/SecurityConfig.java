package com.morewen.gateway.config;

import com.morewen.gateway.security.*;
import com.morewen.gateway.security.handler.CustomAccessDeniedHandler;
import com.morewen.gateway.security.handler.JsonServerAuthenticationFailureHandler;
import com.morewen.gateway.security.handler.JsonServerAuthenticationSuccessHandler;
import com.morewen.gateway.security.handler.JsonServerLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

   @Autowired
   private AuthorizeConfigManager authorizeConfigManager;

   @Autowired
   private JwtAuthenticationFilter jwtAuthenticationFilter;

   @Autowired
   private JsonServerAuthenticationSuccessHandler jsonServerAuthenticationSuccessHandler;

   @Autowired
   private JsonServerAuthenticationFailureHandler jsonServerAuthenticationFailureHandler;

   @Autowired
   private JsonServerLogoutSuccessHandler jsonServerLogoutSuccessHandler;

   @Autowired
   private CustomAccessDeniedHandler customAccessDeniedHandler;
   private static final String[] AUTH_WHITELIST = new String[]{"/login, /logout"};

   @Bean
   public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
       SecurityWebFilterChain chain = http.formLogin()
               .loginPage("/login")
               // 登录成功handler
               .authenticationSuccessHandler(jsonServerAuthenticationSuccessHandler)
               // 登陆失败handler
               .authenticationFailureHandler(jsonServerAuthenticationFailureHandler)
               .and()
               .logout()
               // 登出成功handler
               .logoutSuccessHandler(jsonServerLogoutSuccessHandler)
               .and()
               .csrf().disable()
               .httpBasic().disable()
               .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
               .authorizeExchange()
               // 白名单放行
               .pathMatchers(AUTH_WHITELIST).permitAll()
               // 访问权限控制
               .anyExchange().access(authorizeConfigManager)
               .and()
               .exceptionHandling()
               .accessDeniedHandler(customAccessDeniedHandler) // 添加自定义的访问被拒绝处理器
               .and()
               .build();
       return chain;
   }

   /**
    * BCrypt密码编码
    * @return 
    */
   @Bean
   public BCryptPasswordEncoder bcryptPasswordEncoder() {
       return new BCryptPasswordEncoder();
   }

}