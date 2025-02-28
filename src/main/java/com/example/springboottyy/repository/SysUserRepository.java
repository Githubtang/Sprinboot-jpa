package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Insight
 * @Description: 用户数据访问层
 * @Date: 2024/10/13 0:06
 * @Version: 1.0
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {
    SysUser findByUsername(String username);

    @Query(value = "SELECT DISTINCT u.* FROM sys_user u " +
            "LEFT JOIN user_role ur ON u.id = ur.user_id " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id " +
            "LEFT JOIN sys_dept d ON u.dept_id = d.id " +
            "WHERE u.deleted = false " +
            ":#{#dataScope == null || #dataScope.isEmpty() ? '' : #dataScope}", nativeQuery = true)
    List<SysUser> findAllWithDataScope(@Param("dataScope") String dataScope);
}