package com.example.springboottyy.service;

import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ruoyi
 * @Description: TODO
 * @Date: 2024/10/18 0:18
 * @Version: 1.0
 */
@Component
public class SysPermissionService {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;
    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getId()).getData());
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            Set<SysRole> roles = user.getRoles();
            if (!roles.isEmpty() && roles.size() > 1)
            {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRole role : roles)
                {
                    ApiResponse<Set<String>> response = menuService.selectMenuPermsByRoleId(role.getId());
                    Set<String> rolePerms = response.getData();
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            }
            else
            {
                ApiResponse<Set<String>> response = menuService.selectMenuPermsByUserId(user.getId());
                Set<String> rolePerms = response.getData();
                perms.addAll(rolePerms);
            }
        }
        return perms;
    }
}
