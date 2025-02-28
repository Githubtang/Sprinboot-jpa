package com.example.springboottyy.aspect;

import com.example.springboottyy.annotation.DataScope;
import com.example.springboottyy.model.BaseEntity;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.model.SysDept;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.security.contest.PermissionContextHolder;
import com.example.springboottyy.utils.SecurityUtils;
import com.example.springboottyy.utils.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
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

    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint joinPoint, DataScope controllerDataScope) {
        clearDataScope(joinPoint);
        handleDataScope(joinPoint, controllerDataScope);
    }

    private void handleDataScope(JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(),
                        PermissionContextHolder.getContext());
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(), permission);
            }
        }
    }

    private void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias,
                                 String permission) {
        Specification<SysUser> specification = (root, query, cb) -> {
            String dataScope = permission;
            var subquery = query.subquery(Long.class);
            var subRoot = subquery.from(SysDept.class);
            if (StringUtils.isNotBlank(dataScope)) {
                switch (dataScope) {
                    case DATA_SCOPE_ALL:
                        return null;
                    case DATA_SCOPE_CUSTOM:
                        // 自定义数据权限
                        Join<SysUser, SysDept> deptJoin = root.join("dept", JoinType.LEFT);
                        deptJoin.alias(deptAlias); // 设置部门别名
                        return cb.exists(
                                subquery.select(cb.literal(1L))
                                        .where(cb.and(
                                                cb.equal(subRoot.get("id"), root.get("deptId")),
                                                cb.equal(subRoot.get("id"), user.getDeptId())
                                        ))
                                // .where(cb.equal(deptJoin.get("id"), root.get("deptId")))
                        );
                    case DATA_SCOPE_DEPT:
                        // 本部门数据权限
                        Join<SysUser, SysDept> deptJoin2 = root.join("dept", JoinType.LEFT);
                        deptJoin2.alias(deptAlias);
                        return cb.equal(deptJoin2.get("id"), user.getDeptId());
                    case DATA_SCOPE_DEPT_AND_CHILD:
                        Join<SysUser, SysDept> deptJoin3 = root.join("dept", JoinType.LEFT);
                        deptJoin3.alias(deptAlias);
                        return cb.or(
                                cb.equal(deptJoin3.get("id"), user.getDeptId()),
                                cb.like(deptJoin3.get("ancestors"), user.getDeptId() + ".%"));
                    case DATA_SCOPE_SELF:
                        // 仅本人数据权限
                        if (StringUtils.isNotBlank(userAlias)) {
                            root.alias(userAlias); // 设置别名
                            return cb.equal(root.get("id"), user.getId());
                        } else {
                            return cb.equal(root.get("createBy"), user.getUsername());
                        }
                    default:
                        return null;
                }
            }
            return null;
        };
        // 设置数据过滤
        if (joinPoint.getArgs().length <= 0) {
            return;
        }
        Object params = joinPoint.getArgs()[0];
        if (params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, specification);
        }

    }

    private void clearDataScope(final JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        if (StringUtils.isNotNull(params) && params[0] instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) params[0];
            entity.getParams().put(DATA_SCOPE, null);
        }
    }

}
