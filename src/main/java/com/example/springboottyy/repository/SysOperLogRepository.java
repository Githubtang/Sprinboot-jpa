package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysOperLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: GithubTang
 * @Description: TODO
 * @Date: 2025/3/8 2:24
 * @Version: 1.0
 */
public interface SysOperLogRepository extends JpaRepository<SysOperLog,Long> {
}
