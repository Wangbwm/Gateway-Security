package com.morewen.gateway.security;


import com.morewen.gateway.generator.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 身份认证类
 *
 */
@Slf4j
@Component
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private static final String USER_NOT_EXISTS = "用户不存在！";

    private final AccountInfoRepository accountInfoRepository;

    public ReactiveUserDetailsServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountInfoRepository.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException(USER_NOT_EXISTS))))
                .doOnNext(u -> log.info(
                        String.format("查询账号成功  user:%s password:%s", u.getUsername(), u.getPassword())))
                .cast(UserDetails.class);
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return accountInfoRepository.findByUsername(user.getUsername())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException(USER_NOT_EXISTS))))
                .map(foundedUser -> {
                    String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                    return new User(foundedUser.getUsername(), encodedPassword, foundedUser.getAuthorities());
                })
                .flatMap(accountInfoRepository::save)
                .cast(UserDetails.class);
    }
}