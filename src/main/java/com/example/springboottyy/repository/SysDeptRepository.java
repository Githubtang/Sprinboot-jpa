package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysDept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysDeptRepository extends JpaRepository<SysDept,Long> {
    public List<SysDept> findByParentId(Long parentId);
}
