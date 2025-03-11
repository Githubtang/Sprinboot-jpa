package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 用户表
 * @Date: 2024/10/12 22:27
 * @Version: 1.0
 */
@Schema(description = "用户")
@Getter
@Setter
@ToString
@Entity
public class SysUser extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 部门ID */
    @Schema(title = "部门编号")
    @NotNull(message = "部门编号不能为空")
    private Long deptId;

    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(description = "删除true/未删除false")
    private boolean deleted = Boolean.FALSE;

    @Schema(description = "启用true/禁用false")
    private boolean enabled = Boolean.TRUE;

    @Schema(title = "最后登录IP")
    private String loginIp;

    @Schema(title = "最后登录时间")
    private LocalDateTime loginDate;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return this.getRoles().stream().anyMatch(sysRole -> sysRole.getRoleKey().contains("admin"));
    }

    // 用户角色
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<SysRole> roles;

    // 用户岗位
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_post", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<SysPost> posts;

    // 用户文件
//    @OneToMany(mappedBy = "sysUser", fetch = FetchType.LAZY)
//    private Set<SysFile> files;


    public Long getRoleId() {
        return this.roles != null && !this.roles.isEmpty()
                ? this.roles.iterator().next().getId()
                : null;
    }
}
