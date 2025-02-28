package com.example.springboottyy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 角色表
 */
@Schema(description = "角色表")
@Data
@Entity
public class SysRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "角色名")
    private String roleName;

    @Schema(description = "角色权限")
    private String roleKey;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限） */
    @Schema(title = "数据范围", description = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    @Schema(title = "菜单树选择项是否关联显示", description = "0：父子不互相关联显示 1：父子互相关联显示")
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    @Schema(title = "部门树选择项是否关联显示", description = "0：父子不互相关联显示 1：父子互相关联显示 ")
    private boolean deptCheckStrictly;

    @Schema(description = "删除true/未删除false")
    private boolean deleted = Boolean.FALSE;

    @Schema(description = "启用true/禁用false")
    private boolean enabled = Boolean.TRUE;

    @Schema(title = "用户是否存在此角色标识", description = "默认不存在")
    private boolean flag = false;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @Transient
    @Schema(description = "角色菜单权限")
    private Set<String> permissions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<SysMenu> menus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_dept",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "dept_id"))
    private Set<SysDept> depts;

}
