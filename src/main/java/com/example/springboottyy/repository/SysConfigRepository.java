package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {
    Optional<SysConfig> findByConfigKey(String ConfigKey);
    Optional<SysConfig> findByConfigKeyOrId(String ConfigKey, Long id);
}