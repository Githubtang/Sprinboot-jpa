package com.example.springboottyy.service;

import com.example.springboottyy.dto.UserDto;
import com.example.springboottyy.dto.mapper.UserMapper;
import com.example.springboottyy.model.*;
import com.example.springboottyy.repository.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.nio.file.OpenOption;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: Insight
 * @Date: 2024/10/13 0:05
 * @Version: 1.0
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        for (SysRole role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleKey()));
            for (SysMenu menu : role.getMenus()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + menu.getMenuName()));
            }
        }
        Set<String> permission = permissionService.getMenuPermission(user);
        Long deptId = Optional.ofNullable(user.getDept()).map(SysDept::getId).orElse(null);
        return new LoginUser(
                user.getId(),
                deptId,
                user,
                permission,
                authorities
        );
    }

}