package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/30 0:01
 * @Version: 1.0
 */
@Data
public class SysFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件桶")
    private String bucketName;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    /**
     * 盘符路径
     */
    @Schema(description = "盘符路径")
    private String dirName;

    /**
     * 盘符类型
     */
    @Schema(description = "盘符类型")
    private String sysTypeName;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String typeName;

    /**
     * 总大小
     */
    @Schema(description = "总大小")
    private String total;

    /**
     * 剩余大小
     */
    @Schema(description = "剩余大小")
    private String free;

    /**
     * 已经使用量
     */
    @Schema(description = "已经使用量")
    private String used;

    /**
     * 资源的使用率
     */
    @Schema(description = "资源的使用率")
    private double usage;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SysUser sysUser;

}
