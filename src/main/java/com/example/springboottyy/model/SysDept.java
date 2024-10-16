package com.example.springboottyy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 部门表
 * @Date: 2024/10/12 23:21
 * @Version: 1.0
 */
@Data
@Entity
public class SysDept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "父部门ID")
    private Long parentId;

    /**
     * 父部门名称
     */
    @Transient
    @Schema(title = "父部门名称")
    private String parentName;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "描述")
    private String remake;

    @Schema(description = "删除true/未删除false")
    private boolean deleted = Boolean.FALSE;

    @Schema(description = "启用true/禁用false")
    private boolean enabled = Boolean.TRUE;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @Transient
    @Schema(description = "子部门")
    private List<SysDept> children = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "dept", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SysUser> users;

}
