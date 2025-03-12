package com.example.springboottyy.service;

import com.example.springboottyy.dto.UserDto;
import com.example.springboottyy.dto.mapper.UserMapper;
import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.SysDept;
import com.example.springboottyy.model.SysPost;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.repository.SysDeptRepository;
import com.example.springboottyy.repository.SysPostRepository;
import com.example.springboottyy.repository.SysRoleRepository;
import com.example.springboottyy.repository.SysUserRepository;
import com.example.springboottyy.service.impl.SysConfigService;
import com.example.springboottyy.utils.*;
import com.example.springboottyy.utils.bean.BeanValidators;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Insight
 * @Description: 用户service
 * @Date: 2024/10/13 0:17
 * @Version: 1.0
 */
@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    SysUserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysDeptRepository deptRepository;

    @Autowired
    private SysPostRepository postRepository;

    @Autowired
    private SysConfigService configService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    protected Validator validator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public ApiResponse<?> selectUserList(SysUser user) {
        ApiResponse<?> all = findAll();
        return all;
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */


    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */

    @Transactional
    public ApiResponse<?> findAll() {
        List<SysUser> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ApiResponse.error("No users found");
        }
        List<UserDto> dtos = users.stream().map(user -> {
            String deptName = Objects.requireNonNull(deptRepository.findById(user.getDeptId()).orElse(null))
                    .getDeptName();
            return userMapper.toDto(user, deptName);
        }).collect(Collectors.toList());

        return ApiResponse.success("Users found", dtos);
    }

    public ApiResponse<?> getUserById(Long id) {
        Optional<SysUser> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ApiResponse.error("SysUser not found");
        }
        SysUser sysUser = user.get();
        String deptName = Objects.requireNonNull(deptRepository.findById(sysUser.getDeptId()).orElse(null))
                .getDeptName();

        UserDto dto = userMapper.toDto(user.get(), deptName);
        return ApiResponse.success("Users found", dto);
    }

    public ApiResponse<?> createUser(@NotNull SysUser user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        String deptName = Objects.requireNonNull(deptRepository.findById(user.getDeptId()).orElse(null)).getDeptName();

        UserDto dto = userMapper.toDto(userRepository.save(user), deptName);
        return ApiResponse.success("SysUser created", dto);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /* 删除用户 */
    @Transactional
    public ApiResponse<Boolean> softDeleteUser(Long id) {
        Optional<SysUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.error("user not fount ", false);
        }
        SysUser user = optionalUser.get();
        user.setDeleted(true);
        user.setEnabled(false);
        userRepository.save(user);
        return ApiResponse.success("user deleted", true);
    }


    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public ApiResponse<?> importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (userList.isEmpty()) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsd = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                //验证是否存在这个用户
                SysUser sysUser = userRepository.findByUsername(user.getUsername());
                if (StringUtils.isNull(sysUser)){
                    BeanValidators.validateWithException(validator,user);
                    checkUserAllowed(user);
                }
            }catch (Exception e){

            }
        }
        return null;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    public void checkUserDataScope(Long userId) {
        SysUser sysUser = userRepository.findById(userId).get();
        if (!sysUser.isAdmin()) {
            SysUser user = new SysUser();
            user.setId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /* 开启所有用户 */
    public ApiResponse<ArrayList<SysUser>> upUser() {
        ArrayList<SysUser> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            user.setEnabled(true);
            userRepository.save(user);
            users.add(user);
        });
        return ApiResponse.success("user up", users);
    }

    /* 关闭用户 */
    public ApiResponse<ArrayList<SysUser>> downUsers(List<Long> ids) {
        ArrayList<SysUser> users = new ArrayList<>();
        if (CollectionUtils.isEmpty(ids)) {
            return ApiResponse.error("ids 为空", null);
        }
        userRepository.findAllById(ids).forEach(user -> {
            user.setEnabled(false);
            userRepository.save(user);
            users.add(user);
        });
        return ApiResponse.success("user down", users);
    }

    /* 用户新增角色 */
    @Transactional
    public ApiResponse<?> addUserRole(Long userId, Long roleId) {
        Optional<SysUser> optionalUser = userRepository.findById(userId);
        Optional<SysRole> optionalRole = roleRepository.findById(roleId);
        if (optionalUser.isPresent()) {
            SysUser user = optionalUser.get();
            SysRole role = optionalRole.get();
            user.getRoles().add(role);
            userRepository.save(user);
            return new ApiResponse<>(200, "useraddrole ok", user);
        }
        return new ApiResponse<>(500, "useraddrole filed", null);
    }

    /* 用户查询角色 */
    @Transactional
    public ApiResponse<Set<SysRole>> findRolesByUserId(Long userId) {
        Optional<SysUser> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ApiResponse.error("user not fount ", null);
        }
        SysUser sysUser = user.get();
        Set<SysRole> roles = sysUser.getRoles();
        return ApiResponse.success("查询成功", roles);

    }

    /* 用户新增岗位 */
    @Transactional
    public ApiResponse<?> addUserPost(Long userId, List<Long> postIds) {
        Optional<SysUser> optionalUser = userRepository.findById(userId);
        List<SysPost> posts = postRepository.findAllById(postIds);
        if (optionalUser.isPresent() && !posts.isEmpty()) {
            SysUser user = optionalUser.get();
            user.setPosts(null);
            posts.forEach(post -> {
                user.getPosts().add(post);
            });
            userRepository.save(user);
            return ApiResponse.success("加入岗位成功", user);
        }
        return ApiResponse.error("加入岗位失败");
    }

    /* 用户新增部门 */
    @Transactional
    public ApiResponse<SysUser> addUserDept(Long userId, Long deptId) {
        Optional<SysUser> optionalUser = userRepository.findById(userId);
        Optional<SysDept> optionalDept = deptRepository.findById(deptId);
        if (optionalUser.isPresent() && optionalDept.isPresent()) {
            SysUser user = optionalUser.get();
            SysDept dept = optionalDept.get();
            user.setId(dept.getId());
            userRepository.save(user);
            return ApiResponse.success("加入部门成功", user);
        }
        return ApiResponse.error("加入部门失败");
    }
}
