package com.example.springboottyy.dto;

import lombok.Data;

/**
 * @Author: Insight
 * @Description: TODO 部门dto
 * @Date: 2024/10/14 8:03
 * @Version: 1.0
 */
@Data
public class DeptDto {
    private Long id;
    private String deptName;
    private String description;
    private boolean isDeleted = Boolean.FALSE;
    private boolean enabled = Boolean.TRUE;
}
