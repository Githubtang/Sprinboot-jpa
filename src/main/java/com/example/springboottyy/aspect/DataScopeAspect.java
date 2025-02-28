package com.example.springboottyy.aspect;

import com.example.springboottyy.annotation.DataScope;
import com.example.springboottyy.model.BaseEntity;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.security.contest.PermissionContextHolder;
import com.example.springboottyy.utils.SecurityUtils;
import com.example.springboottyy.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: Insight
 * @Description: 数据过滤处理
 * @Date: 2025/2/26 23:25
 * @Version: 1.0
 */
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";
    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";
    private static final Logger log = LoggerFactory.getLogger(DataScopeAspect.class);


    @Before("@annotation(DataScope)")
    public void doBefore(JoinPoint joinPoint, DataScope controllerDataScope) {
        clearDataScope(joinPoint);
        handleDataScope(joinPoint, controllerDataScope);
    }

    private void handleDataScope(JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionContextHolder.getContext());
                dataScopeFilter(joinPoint,currentUser,controllerDataScope.deptAlias(),controllerDataScope.userAlias(),permission);
            }
        }
    }

    private void dataScopeFilter(JoinPoint joinPoint, SysUser currentUser, String s, String s1, String permission) {
    }

    private void clearDataScope(final JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        if (StringUtils.isNotNull(params) && params[0] instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) params[0];
            entity.getParams().put(DATA_SCOPE, "");
        }
    }

}
