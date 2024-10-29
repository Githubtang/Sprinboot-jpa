package com.example.springboottyy.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Insight
 * @Description: TODO
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
