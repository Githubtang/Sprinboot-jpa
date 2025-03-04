package com.example.springboottyy.controller;

import com.example.springboottyy.domain.Server;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: GithubTang
 * @Description: 服务器监控
 * @Date: 2025/3/4 22:58
 * @Version: 1.0
 */
@Tag(name = "服务器监控")
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    @Operation(summary = "获取服务信息")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> gitInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        ApiResponse<Object> response = ApiResponse.success().put("server", server);
        return ResponseEntity.ok(response);
    }
}
