package com.example.springboottyy.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/16 8:17
 * @Version: 1.0
 */
@Data
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long deptId;
    private String username;
    // 权限列表
    private Set<String> permissions;
    // 用户信息
    private SysUser user;

    private Collection<? extends GrantedAuthority> authorities;

    public LoginUser(Long id, Long deptId, SysUser user,Set<String> permissions, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

}