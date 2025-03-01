package com.example.springboottyy.utils;

import com.example.springboottyy.config.MinioConfig;
import com.example.springboottyy.model.MinioBucket;
import com.example.springboottyy.model.vo.MinioFileVO;
import com.example.springboottyy.model.vo.MinioFilesVo;
import io.minio.GetObjectArgs;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: Insight
 * @Description: minio工具类
 * @Date: 2024/10/27 20:26
 * @Version: 1.0
 */
@Component
public class MinioFileUtil {

    private final MinioConfig minioConfig;

    public MinioFileUtil(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    public String uploadFile(String client, MultipartFile file) throws Exception {
        final String fileName = file.getOriginalFilename();
        return uploadFile(client, fileName, file);
    }

    public String uploadFile(String client, String fileName, MultipartFile file) throws Exception {
        // 上传文件
        this.minioConfig.getBucket(client).put(fileName, file);
        // 返回地址
        return getUrl(client, fileName);
    }

    /* 返回文件路径 */
    public String getUrl(String client, String fileName) {
        return minioConfig.getPrefix() + "/" + client + "?" + "fileName=" + fileName;
    }

    public List<MinioFilesVo> getList(String client) throws Exception {
        MinioBucket bucket = minioConfig.getBucket(client);
        return bucket.list();
    }

    public MinioFileVO downloadFile(String client, String filePath) throws Exception {
        MinioBucket bucket = minioConfig.getBucket(client);
        return bucket.get(GetObjectArgs.builder().bucket(bucket.getBucketName()).object(filePath).build());
    }

    public void removeFile(String client, String filePath) throws Exception {
        minioConfig.getBucket(client).remove(filePath);
    }
}
