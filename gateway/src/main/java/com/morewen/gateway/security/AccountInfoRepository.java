package com.morewen.gateway.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.morewen.gateway.generator.domain.SysUser;
import com.morewen.gateway.generator.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Objects;


@Repository
public class AccountInfoRepository {
    @Autowired
    SysUserService sysUserService;
    public Mono<UserDetails> findByUsername(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",username).last("limit 1");
        SysUser user = sysUserService.getOne(wrapper);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户不存在！");
        }
        // 理论上权限需要从数据库获取，这里就偷懒了
        UserDetails user1 = User.withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities("ROLE_USER", "ROLE_HELLO", "ROLE_ORDER")
                .build();
        return Mono.justOrEmpty(user1);
    }



    public Mono<UserDetails> save(UserDetails user) {
        // 保存账号信息到存储库
        SysUser sysUser = new SysUser(user.getUsername(), user.getPassword());
        if (sysUserService.saveOrUpdate(sysUser)) {
            return Mono.just(user);
        }else return Mono.empty();
    }



}
