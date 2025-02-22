package com.example.springboottyy.model;

import com.example.springboottyy.model.vo.MinioFileVO;
import com.example.springboottyy.model.vo.MinioFilesVo;
import io.minio.*;
import io.minio.messages.Item;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @Author: Insight
 * @Description: minio存储桶
 * @Date: 2024/10/27 16:03
 * @Version: 1.0
 */
@Data
public class MinioBucket {
    private static final Logger log = LoggerFactory.getLogger(MinioBucket.class);
    private MinioClient client;    // MinIO 客户端
    private String bucketName;      // 存储桶名称

    public MinioBucket(MinioClient client, String buketName) {
        this.client = client;
        this.bucketName = buketName;
    }

    public void put(String fileName, MultipartFile file) throws IOException {
        put(fileName, file.getContentType(), file.getInputStream());
    }

    /* 构建minio文件对象 */
    public void put(String filePath, String contentType, InputStream inputStream) throws IOException {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath)
                .stream(inputStream, inputStream.available(), -1)
                .contentType(contentType)
                .build();
        put(args);
    }

    /* 上传文件到minio服务器 */
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

    public List<MinioFilesVo> list() throws Exception {
        ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).build();
        return list(args);
    }

    public List<MinioFilesVo> list(ListObjectsArgs args) throws Exception {
        Iterable<Result<Item>> results = this.client.listObjects(args);
        Stream<Result<Item>> stream = StreamSupport.stream(results.spliterator(), false);
        List<MinioFilesVo> files = stream.map(result -> {
            try {
                MinioFilesVo vo = new MinioFilesVo();
                Item item = result.get();
                vo.setFileName(item.objectName());
                vo.setFilePath(item.objectName());
                vo.setFileSize(item.size());
                vo.setLastModified(Date.from(item.lastModified().toInstant()));
                return vo;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return files;
    }

    public MinioFileVO get(String filePath) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(filePath).build();
        return get(args);
    }

    /* 获取服务器文件 */
    public MinioFileVO get(GetObjectArgs args) throws Exception {
        GetObjectResponse inputStream = this.client.getObject(args);
        return new MinioFileVO(inputStream,
                inputStream.object(),
                inputStream.headers(),
                inputStream.bucket(),
                inputStream.region());
    }
}