package com.example.springboottyy.model;

import com.example.springboottyy.dto.mapper.UserMapper;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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

    public LoginUser(Long id,Long deptId,SysUser user,Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.deptId = deptId;
        this.user = user;
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