package com.example.springboottyy.service;

import com.example.springboottyy.model.SysDept;
import com.example.springboottyy.repository.SysDeptRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

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

    public ApiResponse<?> findAll() {
        List<SysDept> all = buildDeptTree();
        if (ObjectUtils.isEmpty(all)) {
            return ApiResponse.error("find dept filed", null);
        }
        return ApiResponse.success("all dept", all);
    }

    public ApiResponse<?> findDeptById(Long id) {
        SysDept dept = getDeptById(id);
        if (!ObjectUtils.isEmpty(dept)) {
            return ApiResponse.success("find dept by id", dept);
        }
        return ApiResponse.error("查询部门失败", null);
    }

    /**
     * 树形结构的部门列表
     */
    public List<SysDept> buildDeptTree() {
        List<SysDept> deptList  = deptRepository.findAll();
        return buildTree(deptList);
    }

    /**
     * 递归构建树形结构
     */
    public List<SysDept> buildTree(List<SysDept> deptList) {
        List<SysDept> tree = new ArrayList<>();
        Map<Long, SysDept> deptMap = new HashMap<>();
        for (SysDept dept : deptList) {
            deptMap.put(dept.getId(), dept);
        }
        for (SysDept dept : deptList) {
            Long parentId = dept.getParentId();
            if (parentId == null || parentId <= 0) {
                tree.add(dept);
                continue;
            }
            SysDept parent = deptMap.get(parentId);
            if (!ObjectUtils.isEmpty(parent)) {
                parent.getChildren().add(dept);
            }
        }
        return tree;
    }

    /**
     * 获取一个部门部门树
     */
    public SysDept getDeptById(Long id) {
        Optional<SysDept> dept = deptRepository.findById(id);
        if (!dept.isPresent()) {
            return null;
        }
        SysDept sysDept = dept.get();
        List<SysDept> all = deptRepository.findAll();
        // 子部门
        List<SysDept> childrenDeptById = findChildrenDeptById(sysDept.getId(),all);
        List<SysDept> byParentId = findByParentId(sysDept.getId());
        sysDept.setChildren(byParentId);
        return sysDept;
    }

    /**
     * 查询部门的子部门
     */
    public List<SysDept> findChildrenDeptById(Long deptId,List<SysDept> all) {
        List<SysDept> children  = new ArrayList<>();
        for (SysDept dept : all) {
            if (dept.getParentId() != null && dept.getParentId().equals(deptId)) {
                children.add(dept);
                dept.setChildren(findChildrenDeptById(dept.getId(),all));
            }
        }
        return children;
    }

    /**
     * 查询部门的子部门2
     */
    public List<SysDept> findByParentId(Long parentId) {
        ArrayList<SysDept> children = new ArrayList<>();
        List<SysDept> all = deptRepository.findByParentId(parentId);
        for (SysDept dept : all) {
            if (!ObjectUtils.isEmpty(dept) && dept.getParentId() != null && dept.getParentId().equals(parentId)) {
                children.add(dept);
                dept.setChildren(findChildrenDeptById(dept.getId(),all));
            }
        }
        return children;
    }


    /*创建部门*/
    public ApiResponse<?> createDept(SysDept department) {
        if (!StringUtils.hasLength(department.getDeptName())) {
            return ApiResponse.error("部门名不能为空", null);
        }
        SysDept save = deptRepository.save(department);
        return ApiResponse.success("save dept", save);
    }

    /*修改部门*/
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
