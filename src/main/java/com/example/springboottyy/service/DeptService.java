package com.example.springboottyy.service;

import com.example.springboottyy.model.SysDept;
import com.example.springboottyy.repository.SysDeptRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Insight
 * @Description: 部门service
 * @Date: 2024/10/13 1:00
 * @Version: 1.0
 */
@Service
public class DeptService{
    @Autowired
    private SysDeptRepository deptRepository;

    @Cacheable(value = "deptCache")
    public ApiResponse<?> findAll() {
        List<SysDept> all = deptRepository.findAll();
        if (all.isEmpty()) {
            return ApiResponse.error("find dept filed", null);
        }
        return ApiResponse.success("all dept", all);
    }

    public ApiResponse<?> findDeptById(Long id) {
        Optional<SysDept> dept = deptRepository.findById(id);
        if (dept.isPresent()) {
            return ApiResponse.success("find dept by id", dept.get());
        }
        return ApiResponse.error("查询部门失败", null);
    }

    public ApiResponse<?> createDept(SysDept department) {
        if (!StringUtils.hasLength(department.getDeptName())) {
            return ApiResponse.error("部门名不能为空", null);
        }
        SysDept save = deptRepository.save(department);
        return ApiResponse.success("save dept", save);
    }

    public ApiResponse<?> updateDept(SysDept department) {
        Optional<SysDept> dept = deptRepository.findById(department.getId());
        if (dept.isPresent()) {
            SysDept save = deptRepository.save(department);
            return ApiResponse.success("update dept", save);
        }
        return ApiResponse.error("更新部门失败", null);
    }

    /*删除部门*/
    public ApiResponse<?> softDeleteDept(List<Long> ids) {
        List<SysDept> depts = deptRepository.findAllById(ids);
        ArrayList<SysDept> departments = new ArrayList<>();
        if (!ObjectUtils.isEmpty(depts)) {
            for (SysDept dept : depts) {
                dept.setDeleted(true);
                deptRepository.save(dept);
                departments.add(dept);
            }
            return ApiResponse.success("关闭部门成功", departments);
        }
        return ApiResponse.error("删除部门失败",null);
    }

    /*关闭部门*/
    public ApiResponse<?> enabledDept(List<Long> ids) {
        List<SysDept> depts = deptRepository.findAllById(ids);
        ArrayList<SysDept> departments = new ArrayList<>();
        if (!ObjectUtils.isEmpty(depts)) {
            for (SysDept dept : depts) {
                dept.setEnabled(false);
                deptRepository.save(dept);
                departments.add(dept);
            }
            return ApiResponse.success("关闭部门成功", departments);
        }
        return ApiResponse.error("关闭部门失败",null);
    }

    /*开启部门*/
    public ApiResponse<?> unEnabledDept(List<Long> ids) {
        List<SysDept> depts = deptRepository.findAllById(ids);
        ArrayList<SysDept> departments = new ArrayList<>();
        if (!ObjectUtils.isEmpty(depts)) {
            for (SysDept dept : depts) {
                dept.setEnabled(true);
                deptRepository.save(dept);
                departments.add(dept);
            }
            return ApiResponse.success("开启部门成功", departments);
        }
        return ApiResponse.error("开启部门失败",null);
    }
}
