package com.example.springboottyy.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 登录用户信息
 * @Date: 2024/10/16 8:17
 * @Version: 1.0
 */
@Data
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long deptId;
    private String username;

    // 用户唯一标识
    private String token;

    // 登录时间
    private Long loginTime;

    // 过期时间
    private Long expireTime;


    // 登录IP地址
    private String ipaddr;

    // 登录地点
    private String loginLocation;

    // 浏览器类型
    private String browser;

    // 操作系统
    private String os;

    // 权限列表
    private Set<String> permissions;

    // 用户信息
    private SysUser user;

    private Collection<? extends GrantedAuthority> authorities;

    public LoginUser() {
    }

    public LoginUser(SysUser user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long id, Long deptId, SysUser user, Set<String> permissions) {
        this.id = id;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long id, Long deptId, SysUser user, Set<String> permissions,
                     Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "id=" + id +
                ", deptId=" + deptId +
                ", username='" + username + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}