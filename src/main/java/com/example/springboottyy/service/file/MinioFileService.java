package com.example.springboottyy.service.file;

import com.example.springboottyy.config.MinioConfig;
import com.example.springboottyy.model.MinioBucket;
import com.example.springboottyy.model.MinioFileVO;
import com.example.springboottyy.utils.MinioFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Author: Insight
 * @Description: Minio服务类
 * @Date: 2024/10/24 16:43
 * @Version: 1.0
 */
@Service
public class MinioFileService implements FileService {
    private final MinioFileUtil minioFileUtil;
    private final MinioConfig minioConfig;

    public MinioFileService(MinioConfig minioConfig, MinioFileUtil minioFileUtil) {
        this.minioFileUtil = minioFileUtil;
        this.minioConfig = minioConfig;
    }

    @Override
    public String upload(String filePath, MultipartFile file) throws Exception {
        return minioFileUtil.uploadFile(minioConfig.getPrimary(), filePath, file);
    }

    @Override
    public String upload(MultipartFile file, String name) throws Exception {
        return minioFileUtil.uploadFile(minioConfig.getPrimary(), name, file);
    }

    @Override
    public String upload(MultipartFile file) throws Exception {
        return minioFileUtil.uploadFile(minioConfig.getPrimary(), file);
    }

    @Override
    public String upload(String baseDir, String fileName, MultipartFile file) throws Exception {
        return minioFileUtil.uploadFile(minioConfig.getPrimary(), baseDir + "/" + fileName, file);
    }

    @Override
    public InputStream downLoad(String filePath) throws Exception {
        MinioFileVO minioFileVO = minioFileUtil.downloadFile(minioConfig.getPrimary(), filePath);
        return minioFileVO.getFileInputSteam();
    }

    @Override
    public boolean deleteFile(String filePath) throws Exception {
        try {
            minioFileUtil.removeFile(minioConfig.getPrimary(), filePath);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage());
        }
    }

}



