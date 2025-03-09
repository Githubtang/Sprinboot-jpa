package com.example.springboottyy.service;

import com.example.springboottyy.exception.ResourceNotFoundException;
import com.example.springboottyy.model.SysMenu;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.repository.SysMenuRepository;
import com.example.springboottyy.repository.SysRoleRepository;
import com.example.springboottyy.repository.SysUserRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @Author: Insight
 * @Description: 角色service
 * @Date: 2024/10/13 0:29
 * @Version: 1.0
 */
@Service
public class RoleService {
    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysMenuRepository menuRepository;

    @Autowired
    private SysUserRepository userRepository;
    
    public List<SysRole> findAll() {
        return roleRepository.findAll();
    }

    public SysRole findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("role not found" + id));
    }

    public SysRole createRole(SysRole role) {
        return roleRepository.save(role);
    }

    // 修改
    public ApiResponse<SysRole> updateRole(SysRole role) {
        Optional<SysRole> optionalRole = roleRepository.findById(role.getId());
        return optionalRole.map(value -> ApiResponse.success("修改角色成功", value)).orElseGet(() -> ApiResponse.error("修改角色失败", null));
    }

    // 删除
    public ApiResponse<?> softDeleteRole(Long id) {
        Optional<SysRole> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            SysRole role = optionalRole.get();
            role.setDeleted(false);
            roleRepository.save(role);
            return new ApiResponse<>(200, "SysRole deleted", role);
        }
        return new ApiResponse<>(400, "role not font", null);
    }

    // 获取用户角色
    public ApiResponse<Set<SysRole>> getRolesUser(SysUser user) {
        Optional<SysUser> optionalSysUser = userRepository.findById(user.getId());
        if (optionalSysUser.isPresent()) {
            SysUser sysUser = optionalSysUser.get();
            Set<SysRole> roles = sysUser.getRoles();
            return new ApiResponse<>(200, "roles", roles);
        }
        return ApiResponse.error("role not font");
    }

    // 角色添加菜单
    public ApiResponse<?> addMenuToRole(Long roleId, Set<Long> menuIds) {
        Optional<SysRole> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty()) {
            return new ApiResponse<>(400, "role not font", null);
        }
        SysRole role = optionalRole.get();

        // 获取并验证所以权限(菜单)
        List<SysMenu> menus = menuRepository.findAllById(menuIds);
        if (menus.isEmpty()) {
            role.setMenus(new HashSet<>());
            return new ApiResponse<>(400, "permission not font", null);
        }
        role.setMenus(new LinkedHashSet<>(menus));
        roleRepository.save(role);
        return new ApiResponse<>(200, "menus added successfully", null);
    }

    /*根据用户id查询角色权限*/
    public ApiResponse<Set<String>> selectRolePermissionByUserId(Long userId) {
        Optional<SysUser> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            SysUser user = optional.get();
            Set<SysRole> roles = user.getRoles();
            Set<String> perms = getPermissionsFromRoles(roles);
            return ApiResponse.success("查询成功", perms);
        }
        return ApiResponse.error("查询失败");
    }

    /*从角色集合中提取权限*/
    public Set<String> getPermissionsFromRoles(Set<SysRole> roles) {
        Set<String> perms = new HashSet<>();
        for (SysRole role : roles) {
           if(!ObjectUtils.isEmpty(role)) {
               perms.addAll(Arrays.asList(role.getRoleKey().trim().split(",")));
           }
        }
        return perms;
    }

}
