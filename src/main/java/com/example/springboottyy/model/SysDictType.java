package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 字典类型实体类
 * @Date: 2024/10/12 23:42
 * @Version: 1.0
 */
@Schema(title = "字典类型")
@Data
@Entity
public class SysDictType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "启用true/禁用false")
    private boolean enabled = Boolean.TRUE;

    @Schema(description = "删除true/未删除false")
    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "dictType",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<SysDictData> dictData;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @Schema(description = "描述")
    private String remark;

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
