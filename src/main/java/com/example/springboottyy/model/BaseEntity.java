package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Insight
 * @Description: 这个类暂时不加入项目 jpa数据库设计
 * @Date: 2025/2/20 16:16
 * @Version: 1.0
 */
@Schema(description = "基类") // api文档描述
@Data
@MappedSuperclass // 表明这是一个基础类，不会单独生成表
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键生成策略
    private Long id;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    @PrePersist  // 创建对象时会回调
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate  // 修改对象时会回调
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
