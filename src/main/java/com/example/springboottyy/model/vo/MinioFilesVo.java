package com.example.springboottyy.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/11/5 15:49
 * @Version: 1.0
 */
@Data
public class MinioFilesVo {
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Date  lastModified;
}
