package com.example.springboottyy.dto;

import com.example.springboottyy.model.SysDept;
import com.example.springboottyy.model.SysRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Insight
 * @Description: userDto
 * @Date: 2024/10/13 0:20
 * @Version: 1.0
 */
@Data
public class UserDto {

    private Long id;
    private String username;
    private String description;
    private boolean deleted;
    private boolean enabled;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long deptId;
    private String deptName;
    private SysDept dept;
    
    private List<Long> roleIds;
    private List<SysRole> roles;
}
