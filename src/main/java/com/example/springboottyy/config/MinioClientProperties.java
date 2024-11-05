package com.example.springboottyy.config;

import io.minio.MinioClient;
import lombok.Data;

/**
 * @Author: Insight
 * @Description: minio客户端配置
 * @Date: 2024/10/27 15:30
 * @Version: 1.0
 */
@Data
public class MinioClientProperties {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    private MinioClient client;

}
