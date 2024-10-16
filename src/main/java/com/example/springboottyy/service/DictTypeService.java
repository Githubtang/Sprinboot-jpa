package com.example.springboottyy.service;

import com.example.springboottyy.model.SysDictData;
import com.example.springboottyy.model.SysDictType;
import com.example.springboottyy.repository.SysDictDataRepository;
import com.example.springboottyy.repository.SysDictTypeRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: Insight
 * @Description: 字典类型service
 * @Date: 2024/10/13 1:18
 * @Version: 1.0
 */
@Service
public class DictTypeService {
    @Autowired
    private SysDictTypeRepository dictTypeRepository;

    public ApiResponse<?> findAllDictTypes() {
        List<SysDictType> dictTypeList = dictTypeRepository.findAll();
        if (ObjectUtils.isEmpty(dictTypeList)) {
            return ApiResponse.error("字典类型列表为空", null);
        }
        return ApiResponse.success("字典类型列表", dictTypeList);
    }

    public ApiResponse<?> findByDictTypeId(Long dictTypeId) {
        Optional<SysDictType> dictType = dictTypeRepository.findById(dictTypeId);
        if (dictType.isEmpty()) {
            return ApiResponse.error("字典类型为空", null);
        }
        return ApiResponse.success("字典类型", dictType);
    }

    public ApiResponse<?> createDictType(SysDictType dictType) {
        if (ObjectUtils.isEmpty(dictType)) {
            return ApiResponse.error("传入字典类型对象为空", null);
        }
        SysDictType save = dictTypeRepository.save(dictType);
        return ApiResponse.success("新增字典类型成功", save);
    }

    public ApiResponse<?> updateDictType(SysDictType dictType) {
        if (ObjectUtils.isEmpty(dictType)) {
            return ApiResponse.error("传入字典类型对象为空", null);
        }
        Optional<SysDictType> dictType1 = dictTypeRepository.findById(dictType.getId());
        if (dictType1.isEmpty()) {
            return ApiResponse.error();
        }
        SysDictType updated = dictTypeRepository.save(dictType);
        return ApiResponse.success("修改字典类型成功", updated);
    }

    /*停用字典类型*/
    public ApiResponse<?> enableDictType(List<Long> ids) {
        List<SysDictType> dictTypeList = dictTypeRepository.findAllById(ids);
        if (ObjectUtils.isEmpty(dictTypeList)) {
            return ApiResponse.error();
        }
        for (SysDictType dictType : dictTypeList) {
            dictType.setEnabled(false); // true启用 false停用
        }
        dictTypeRepository.saveAll(dictTypeList);
        return ApiResponse.success("停用成功", dictTypeList);
    }

    /*启用字典类型*/
    public ApiResponse<?> unEnableDictType(List<Long> ids) {
        List<SysDictType> dictTypeList = dictTypeRepository.findAllById(ids);
        if (ObjectUtils.isEmpty(dictTypeList)) {
            return ApiResponse.error();
        }
        for (SysDictType dictType : dictTypeList) {
            dictType.setEnabled(true); // true启用 false停用
        }
        dictTypeRepository.saveAll(dictTypeList);
        return ApiResponse.success("启用成功", dictTypeList);
    }

    /*获取字典类型下的所以字典数据*/
    public ApiResponse<?> findAllDataOfType(Long dictTypeId) {
        Optional<SysDictType> dictType = dictTypeRepository.findById(dictTypeId);
        if (dictType.isEmpty()) {
            return ApiResponse.error();
        }
        Set<SysDictData> dictData = dictType.get().getDictData();
        if (ObjectUtils.isEmpty(dictData)) {
            return ApiResponse.error("字典数据为空");
        }
        return ApiResponse.success("查询成功",dictData);
    }
}
