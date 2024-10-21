package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysPost;
import com.example.springboottyy.service.PostService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/14 8:06
 * @Version: 1.0
 */
@Tag(name = "岗位管理")
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(description = "岗位列表")
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> getAllPost() {
        ApiResponse<?> response = postService.findAllPost();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取岗位")
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @PostMapping("/getPost")
    public ResponseEntity<ApiResponse<?>> getPostById(@RequestBody Long postId) {
        ApiResponse<?> response = postService.findPostById(postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "创建岗位")
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @PostMapping("/createPost")
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody SysPost post) {
        ApiResponse<?> response = postService.createPost(post);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "修改岗位")
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @PostMapping("/updatePost")
    public ResponseEntity<ApiResponse<?>> updatePost(@RequestBody SysPost post) {
        ApiResponse<?> response = postService.updatePost(post);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "删除岗位")
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @PostMapping("/deletePost")
    public ResponseEntity<ApiResponse<?>> deletePost(@RequestBody List<Long> postIds) {
        ApiResponse<?> response = postService.softDeletePost(postIds);
        return ResponseEntity.ok(response);
    }

    /**
     * 关闭岗位
     */
    @Operation(summary = "关闭岗位")
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @PostMapping("/enablePost")
    public ResponseEntity<ApiResponse<?>> enablePost(@RequestBody List<Long> postIds) {
        ApiResponse<?> response = postService.enabledPost(postIds);
        return ResponseEntity.ok(response);
    }

    /**
     * 开启岗位
     */
    @Operation(summary = "开启岗位")
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @PostMapping("/unEnablePost")
    public ResponseEntity<ApiResponse<?>> unEnablePost(@RequestBody List<Long> postIds) {
        ApiResponse<?> response = postService.unEnabledPost(postIds);
        return ResponseEntity.ok(response);
    }
}
