package com.example.springboottyy.service;

import com.example.springboottyy.model.SysMenu;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.repository.SysMenuRepository;
import com.example.springboottyy.repository.SysRoleRepository;
import com.example.springboottyy.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboottyy.utils.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 菜单service
 * @Date: 2024/10/13 0:35
 * @Version: 1.0
 */
@Service
public class MenuService {
    @Autowired
    private SysMenuRepository menuRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private RoleService roleService;
    @Autowired
    private SysMenuRepository sysMenuRepository;

    public ApiResponse<?> findAll() {
        List<SysMenu> all = menuRepository.findAll();
        if (all.isEmpty()) {
            return new ApiResponse<>("error", "menu not font", null);
        }
        return new ApiResponse<>("success", "menu font", all);
    }

    public ApiResponse<?> findAllById(List<Long> menuIds) {
        List<SysMenu> all = menuRepository.findAllById(menuIds);
        if (all.isEmpty()) {
            return new ApiResponse<>("error", "menu not font", null);
        }
        return new ApiResponse<>("success", "menu font", all);
    }

    public ApiResponse<?> findById(Long id) {
        Optional<SysMenu> permission = menuRepository.findById(id);
        return permission.map(value -> new ApiResponse<>("success", "menu font", value)).orElseGet(() -> new ApiResponse<>("error", "menu not font", null));
    }

    public ApiResponse<?> createMenu(SysMenu menu) {
        SysMenu menu1 = menuRepository.save(menu);
        return new ApiResponse<>("success", "create ok", menu1);
    }

    public ApiResponse<?> softDeleteMenu(Long id) {
        Optional<SysMenu> menu = menuRepository.findById(id);
        if (menu.isPresent()) {
            SysMenu menu1 = menu.get();
            menu1.setDeleted(true);
            SysMenu save = menuRepository.save(menu1);
            return ApiResponse.success("删除成功",save);
        }
        return ApiResponse.error("删除失败",null);
    }

    /**
     * TODO 这里存在互调用明天改一下
     * @param user
     * @return
     */
    // 获取角色菜单
    public ApiResponse<?> getRoleMenu(SysUser user) {
        ApiResponse<Set<SysRole>> response = roleService.getRolesUser(user);
        Set<SysRole> roles = response.getData();
        ArrayList<Long> ids = new ArrayList<>();
        for (SysRole role : roles) {
            ids.add(role.getId());
        }
        List<SysMenu> menus = sysMenuRepository.findAllById(ids);
        return ApiResponse.success("menus font", menus);
    }


    public ApiResponse<?> updateMenu(SysMenu sysMenu) {
        Optional<SysMenu> menu = menuRepository.findById(sysMenu.getId());
        if (menu.isPresent()) {
            SysMenu save = menuRepository.save(sysMenu);
            return ApiResponse.success("update ok", save);
        }
        return ApiResponse.error();
    }
}
