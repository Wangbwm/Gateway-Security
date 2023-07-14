package com.morewen.gateway.generator.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.morewen.gateway.generator.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private SysUser user;
    private List<String> permission;

    public LoginUser(SysUser user,List<String> permission) {
        this.user = user;
        this.permission=permission;
    }
    @JSONField(serialize = false)
    private List<GrantedAuthority>  authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities=new ArrayList<>();
        //注意 为什么这里不直接返回
        for (String perm : permission) {
            authorities.add(new SimpleGrantedAuthority(perm));
        }
        return authorities; //该用户有哪些权限
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {//帐号是不是没有过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //是不是没有被锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //凭证是不是没有过期
        return true;
    }

    @Override
    public boolean isEnabled() {  //是否可用
        return true;
    }
}
