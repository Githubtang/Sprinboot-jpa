package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Insight
 * @Description: 菜单表
 * @Date: 2024/10/12 22:31
 * @Version: 1.0
 */
@Data
@Entity
public class SysMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "菜单名不能为空")
    @Schema(description = "菜单名称")
    private String menuName;

    @Transient
    @Schema(description = "父菜单名称")
    private String parentName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "是否为外链（0是 1否）")
    private int isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private int isCache;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    @Column(length = 1)
    private char menuType;

    @Schema(description = "菜单状态(true删除 false正常)")
    private boolean deleted = Boolean.FALSE;

    @Schema(description = "菜单状态(true启用 false停用)")
    private boolean enabled = Boolean.TRUE;

    /**
     * 权限字符串
     */
    @Schema(description = "权限字符串")
    private String perms;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    /**
     * 子菜单
     */
    @Transient
    @Schema(title = "子菜单")
    private List<SysMenu> children = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
