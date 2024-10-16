package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysDictType;
import com.example.springboottyy.service.DictTypeService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Insight
 * @Description: 字典(类型)controller
 * @Date: 2024/10/14 8:08
 * @Version: 1.0
 */
@Tag(name = "字典(类型)管理")
@RestController
@RequestMapping("/api/dicttype")
public class DictTypeController {
    @Autowired
    private DictTypeService dictTypeService;

    @Operation(summary = "获取字典类型列表")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> getAllDictType() {
        ApiResponse<?> response = dictTypeService.findAllDictTypes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取字典类型")
    @PostMapping("/getDictType")
    public ResponseEntity<ApiResponse<?>> getDictTypeById(@RequestBody Long id) {
        ApiResponse<?> response = dictTypeService.findByDictTypeId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("/addDictType")
    public ResponseEntity<ApiResponse<?>> addDictType(@RequestBody SysDictType dictType) {
        ApiResponse<?> response = dictTypeService.createDictType(dictType);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "修改字典类型")
    @PostMapping("/updateDictType")
    public ResponseEntity<ApiResponse<?>> updateDictType(@RequestBody SysDictType dictType) {
        ApiResponse<?> response = dictTypeService.updateDictType(dictType);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "停用字典类型")
    @PostMapping("/enableDictType")
    public ResponseEntity<ApiResponse<?>> enableDictType(@RequestBody List<Long> ids) {
        ApiResponse<?> response = dictTypeService.enableDictType(ids);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "启用字典类型")
    @PostMapping("/unEnableDictType")
    public ResponseEntity<ApiResponse<?>> unEnableDictType(@RequestBody List<Long> ids) {
        ApiResponse<?> response = dictTypeService.unEnableDictType(ids);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取字典类型下的所以字典数据")
    @PostMapping("/findAllDataOfType")
    public ResponseEntity<ApiResponse<?>> getAllDataOfType(@RequestBody Long id) {
        ApiResponse<?> response = dictTypeService.findAllDataOfType(id);
        return ResponseEntity.ok(response);
    }
}
