package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 0:06
 * @Version: 1.0
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}
