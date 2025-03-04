package com.example.springboottyy.service.file;

import com.example.springboottyy.config.MinioConfig;
import com.example.springboottyy.model.vo.MinioFileVO;
import com.example.springboottyy.model.SysFile;
import com.example.springboottyy.model.vo.MinioFilesVo;
import com.example.springboottyy.utils.MinioFileUtil;
import com.example.springboottyy.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: Insight
 * @Description: Minio服务类
 * @Date: 2024/10/24 16:43
 * @Version: 1.0
 */
@Service
public class MinioFileService implements FileService {
    private static final Logger log = LoggerFactory.getLogger(MinioFileService.class);
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
        return minioFileUtil.uploadFile(minioConfig.getPrimary(), baseDir + File.separator + fileName, file);
    }

    @Override
    public InputStream downLoad(String filePath) throws Exception {
        MinioFileVO minioFileVO = minioFileUtil.downloadFile(minioConfig.getPrimary(), filePath);
        return minioFileVO.getFileInputSteam();
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            minioFileUtil.removeFile(minioConfig.getPrimary(), filePath);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage());
        }
    }

    /* 文件列表 */
    public List<MinioFilesVo> getListFile() throws Exception {
        return minioFileUtil.getList(minioConfig.getPrimary());
    }

    public MinioFileVO getFile(String filePath) throws Exception {
        return minioFileUtil.downloadFile(minioConfig.getPrimary(), filePath);
    }

    @Transactional
    public String uploadAndSaveFile(MultipartFile file) throws Exception {
        String uri = upload(file);
        SysFile sysFile = new SysFile();
        sysFile.setFileName(file.getOriginalFilename());
        sysFile.setFileType(file.getContentType());
        sysFile.setFilePath(uri);
        sysFile.setFileSize(file.getSize());
        sysFile.setSysUser(SecurityUtils.getLoginUser().getUser());
        sysFile.setBucketName(minioConfig.getMasterBucket().getBucketName());
//        fileRepository.save(sysFile);
        return uri;
    }
}



