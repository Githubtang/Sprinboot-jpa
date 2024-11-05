package com.example.springboottyy.controller;

import com.example.springboottyy.model.vo.MinioFileVO;
import com.example.springboottyy.model.vo.MinioFilesVo;
import com.example.springboottyy.service.file.MinioFileService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.Headers;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/24 16:45
 * @Version: 1.0
 */
@Tag(name = "文件管理")
@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private MinioFileService minioService;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    private ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadPath = minioService.upload(file);
            return ResponseEntity.ok(ApiResponse.success(uploadPath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "上传文件并关联")
    @PostMapping("/uploadAndSaveFile")
    private ResponseEntity<ApiResponse<?>> uploadAndSaveFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadUrl = minioService.uploadAndSaveFile(file);
            return ResponseEntity.ok(ApiResponse.success(uploadUrl));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "文件列表")
    @GetMapping("/list")
    private ResponseEntity<ApiResponse<?>> getFileList() {
        try {
            List<MinioFilesVo> listFile = minioService.getListFile();
            return ResponseEntity.ok(ApiResponse.success());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "下载文件")
    @GetMapping("/download")
    private void downloadFile(@RequestParam("filename") String filename, HttpServletResponse response) {
        try {
            MinioFileVO minioFileVO = minioService.getFile(filename);
            Headers headers = minioFileVO.getHeaders();
            response.setContentType(headers.get("content-Type")); // 类型不明用application/octet-stream
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            response.setHeader("Content-Length", headers.get("content-length"));
            IOUtils.copy(minioFileVO.getFileInputSteam(), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "删除文件")
    @PostMapping("/delete")
    private ResponseEntity<ApiResponse<?>> deleteFile(@RequestParam("filename") String filename) {
        try {
            boolean b = minioService.deleteFile(filename);
            if (b) {
                return ResponseEntity.ok(ApiResponse.success("删除成功", b));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.error("错误"));
    }
}
