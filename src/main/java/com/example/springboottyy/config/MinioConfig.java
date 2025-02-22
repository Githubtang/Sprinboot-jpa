package com.example.springboottyy.config;

import com.example.springboottyy.model.MinioBucket;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Insight
 * @Description: minio配置类
 * @Date: 2024/10/24 11:23
 * @Version: 1.0
 */

@Data
@Configuration
@ConfigurationProperties("minio")
public class MinioConfig implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MinioConfig.class);
    private Map<String, MinioClientProperties> client = new HashMap<>();
    private String prefix = "/api/file";
    private String primary;
    private Map<String, MinioBucket> targetMinioBucket = new HashMap<>();
    private MinioBucket masterBucket;

    @Override
    public void afterPropertiesSet() throws Exception {
        client.forEach((name, props) -> targetMinioBucket.put(name, createMinioClient(name, props)));
        if (targetMinioBucket.get(primary) == null) {
            throw new MinioException("Primary client " + primary + " does not exist");
        }

        masterBucket = targetMinioBucket.get(primary);
    }

    private MinioBucket createMinioClient(String name, MinioClientProperties props) {
        MinioClient client;
        if (!StringUtils.hasLength(props.getAccessKey())) {
            client = MinioClient.builder()
                    .endpoint(props.getUrl())
                    .build();
        } else {
            client = MinioClient.builder()
                    .endpoint(props.getUrl())
                    .credentials(props.getAccessKey(), props.getSecretKey())
                    .build();
        }
        MinioBucket minioBucket = new MinioBucket(client, props.getBucketName());
        validateMinioBucket(minioBucket);
        log.info("数据桶：{}  - 链接成功", name);
        return minioBucket;
    }

    private void validateMinioBucket(MinioBucket minioBucket) {
        try {
            // Check if the bucket exists
            if (!minioBucket.getClient().bucketExists(BucketExistsArgs.builder().bucket(minioBucket.getBucketName()).build())) {
                throw new RuntimeException("Bucket " + minioBucket.getBucketName() + " does not exist");
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error checking bucket: " + e.getMessage());
        }
    }

    public MinioBucket getBucket(String client){
        return this.targetMinioBucket.get(client);
    }
}
