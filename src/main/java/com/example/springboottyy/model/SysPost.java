package com.example.springboottyy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 岗位表
 * @Date: 2024/10/12 23:25
 * @Version: 1.0
 */
@Data
@Entity
public class SysPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 65, nullable = false)
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "删除true/未删除false")
    private boolean deleted = Boolean.FALSE;

    @Schema(description = "启用true/禁用false")
    private boolean enabled = Boolean.TRUE;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @Transient
    @Schema(description = "用户是否存在此岗位标识")
    private boolean flag = Boolean.FALSE;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<SysMenu> menus;

    @Schema(description = "描述")
    private String remark;
}
