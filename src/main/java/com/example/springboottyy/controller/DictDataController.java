package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysDictData;
import com.example.springboottyy.service.DictDataService;
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
 * @Description: 字典(数据)controller
 * @Date: 2024/10/14 8:10
 * @Version: 1.0
 */
@Tag(name = "字典数据(管理)")
@RestController
@RequestMapping("/api/dictdata")
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    @Operation(summary = "新增字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping("/addDictData")
    public ResponseEntity<ApiResponse<?>> addDictData(@RequestBody SysDictData dictData) {
        ApiResponse<?> response = dictDataService.createDictData(dictData);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "修改字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PostMapping("/updateDictData")
    public ResponseEntity<ApiResponse<?>> updateDictData(@RequestBody SysDictData dictData) {
        ApiResponse<?> response = dictDataService.updateDictData(dictData);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "(批量)删除字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @PostMapping("/deleteDictData")
    public ResponseEntity<ApiResponse<?>> deleteDictData(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(dictDataService.deleteDictData(ids));
    }
}
