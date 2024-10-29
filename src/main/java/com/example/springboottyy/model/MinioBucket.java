package com.example.springboottyy.model;

import io.minio.*;
import lombok.Data;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/27 16:03
 * @Version: 1.0
 */
@Data
public class MinioBucket {
    private MinioClient client;    // MinIO 客户端
    private String bucketName;      // 存储桶名称

    public MinioBucket(MinioClient client, String buketName) {
        this.client = client;
        this.bucketName = buketName;
    }

    public void put(String fileName, MultipartFile file) throws IOException {
        put(fileName, file.getContentType(), file.getInputStream());
    }

    public void put(String filePath, String contentType, InputStream inputStream) throws IOException {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .stream(inputStream, inputStream.available(), -1)
                .contentType(contentType)
                .build();
        put(args);

    }

    public void put(PutObjectArgs args) {
        try {
            this.client.putObject(args);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void remove(String filePath) throws Exception {
        RemoveObjectArgs build = RemoveObjectArgs.builder().bucket(bucketName).object(filePath).build();
        remove(build);
    }

    public void remove(RemoveObjectArgs args) throws Exception {
        this.client.removeObject(args);
    }

    public MinioFileVO get(String filePath) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(filePath).build();
        return get(args);
    }

    public MinioFileVO get(GetObjectArgs args) throws Exception {
        GetObjectResponse inputStream = this.client.getObject(args);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
        }
        return new MinioFileVO(inputStream,
                inputStream.object(),
                inputStream.headers(),
                inputStream.bucket(),
                inputStream.region());
    }
}