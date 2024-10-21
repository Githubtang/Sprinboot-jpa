package com.example.springboottyy.service;

import com.example.springboottyy.common.constant.Constants;
import com.example.springboottyy.common.constant.UserConstants;
import com.example.springboottyy.model.SysMenu;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.model.vo.MetaVo;
import com.example.springboottyy.model.vo.RouterVo;
import com.example.springboottyy.repository.SysMenuRepository;
import com.example.springboottyy.repository.SysRoleRepository;
import com.example.springboottyy.repository.SysUserRepository;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private SysUserRepository userRepository;

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
            return ApiResponse.success("删除成功", save);
        }
        return ApiResponse.error("删除失败", null);
    }

    public ApiResponse<?> updateMenu(SysMenu sysMenu) {
        Optional<SysMenu> menu = menuRepository.findById(sysMenu.getId());
        if (menu.isPresent()) {
            SysMenu save = menuRepository.save(sysMenu);
            return ApiResponse.success("update ok", save);
        }
        return ApiResponse.error();
    }

    /**
     * TODO 这里存在互调用明天改一下
     *
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
        return ApiResponse.success("查询成功", menus);
    }

    /*根据角色id查询菜单*/
    public ApiResponse<Set<String>> selectMenuPermsByRoleId(Long roleId) {
        Optional<SysRole> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            Set<SysMenu> menus = role.get().getMenus();
            Set<String> perms = new HashSet<>();
            for (SysMenu menu : menus) {
                perms.add(menu.getPerms());
            }
            return ApiResponse.success("查询成功", perms);
        } else {
            return ApiResponse.error("查询失败");
        }
    }

    /*根据用户id查询菜单*/
    public ApiResponse<Set<String>> selectMenuPermsByUserId(Long userId) {
        Optional<SysUser> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            return ApiResponse.error("查询失败");
        }
        SysUser sysUser = optional.get();
        Set<SysRole> roles = sysUser.getRoles();
        Set<String> perms = getPermissionsFromRoles(roles);
        return ApiResponse.success("查询成功", perms);
    }

    /*从角色集合中提取权限*/
    public Set<String> getPermissionsFromRoles(Set<SysRole> roles) {
        Set<String> perms = new HashSet<>();
        for (SysRole role : roles) {
            Set<SysMenu> menus = role.getMenus();
            for (SysMenu menu : menus) {
                if (menu.getPerms().contains("*")) {
                    perms.add(menu.getPerms());
                }
            }
        }
        return perms;
    }

    /*构建前端路由所需要的菜单*/
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()),
                    menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(),
                        StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path       路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0
                && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, "."},
                new String[]{"", "", "", "/"});
    }


}
