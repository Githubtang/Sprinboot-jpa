package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Insight
 * @Description: 字典数据实体类
 * @Date: 2024/10/12 23:42
 * @Version: 1.0
 */
@Schema(title = "字典数据")
@Data
@Entity
public class SysDictData {
    @Schema(description = "字典编码")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictCode;

    private Integer dictSort;

    @Schema(description = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    private String dictLabel;

    @Schema(description = "字典键值")
    @NotBlank(message = "字典键值不能为空")
    private String dictValue;

    @Schema(description = "启用true/禁用false")
    private boolean enabled = Boolean.TRUE;

    @Schema(description = "删除true/未删除false")
    private boolean isDeleted = Boolean.FALSE;

    /** 样式属性（其他样式扩展） */
    @Schema(title = "样式属性", description = "其他样式扩展")
    private String cssClass;

    /** 表格字典样式 */
    @Schema(title = "表格字典样式")
    private String listClass;

    /** 是否默认（Y是 N否） */
    @Schema(title = "是否默认", description = "Y=是,N=否")
    private String isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_type_id", nullable = false)
    private SysDictType dictType;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Schema(description = "描述")
    private String remark;

}
