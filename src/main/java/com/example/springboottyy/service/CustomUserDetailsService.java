package com.example.springboottyy.service;

import com.example.springboottyy.enums.UserStatus;
import com.example.springboottyy.enums.UserStatusJpa;
import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.model.SysMenu;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.repository.SysUserRepository;
import com.example.springboottyy.utils.StringUtils;
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

import java.util.HashSet;
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
    private SysPasswordService passwordService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userRepository.findByUsername(username);
        if (StringUtils.isNull(sysUser)) {
            log.info("登录用户: {} 不存在",username);
            throw new ServiceException("登录用户: "+ username +" 不存在");
        } else if (UserStatusJpa.DELETED.getCode().equals(sysUser.isDeleted())) {
            log.info("登录用户: {} 已被删除",username);
            throw new ServiceException("对不起,你的账号: "+ username +" 已被删除");
        } else if (UserStatusJpa.DISABLE.getCode().equals(sysUser.isEnabled())) {
            log.info("登录用户: {} 已被停用",username);
            throw new ServiceException("对不起,你的账号: "+ username +" 已停用");
        }
        passwordService.validate(sysUser);
        return createUser(sysUser);
    }
    public UserDetails createUser(SysUser user) {
        return new LoginUser(user.getId(),user.getId(),user,permissionService.getMenuPermission(user));
    }
/*  用户信息 version :1.0
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
        Long deptId = user.getDeptId(); // 可能为null
        return new LoginUser(
                user.getId(),
                deptId,
                user,
                permission,
                authorities
        );
    }*/

}