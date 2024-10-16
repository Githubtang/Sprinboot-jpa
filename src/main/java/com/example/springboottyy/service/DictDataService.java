package com.example.springboottyy.service;

import com.example.springboottyy.model.SysDictData;
import com.example.springboottyy.model.SysDictType;
import com.example.springboottyy.repository.SysDictDataRepository;
import com.example.springboottyy.repository.SysDictTypeRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Insight
 * @Description: 字典数据service
 * @Date: 2024/10/13 1:21
 * @Version: 1.0
 */
@Service
public class DictDataService {
    @Autowired
    private SysDictDataRepository dictDataRepository;

    @Autowired
    private SysDictTypeRepository dictTypeRepository;

    /*分页查询*/
    public ApiResponse<?> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SysDictData> dataPage = dictDataRepository.findAll(pageable);
        return ApiResponse.success("查询成功", dataPage);
    }

    public ApiResponse<?> findById(Long id) {
        Optional<SysDictData> dictDataOptional = dictDataRepository.findByDictCode(id);
        if (dictDataOptional.isEmpty()) {
            return ApiResponse.error("查询失败/未找到数据",null);
        }
        return ApiResponse.success("查询成功", dictDataOptional.get());
    }

    /*新增字典数据信息*/
    public ApiResponse<?> createDictData(SysDictData dictData) {
        Optional<SysDictType> dictType = dictTypeRepository.findById(dictData.getDictType().getId());
        if (dictType.isPresent()) {
            SysDictType dictType1 = dictType.get();

            dictData.setDictType(dictType1);
            SysDictData save = dictDataRepository.save(dictData);
            return ApiResponse.success("新增数据字典(数据)成功",save);
        }
        return ApiResponse.error("新增数据字典(数据)失败",null);
    }

    /*修改保存字典数据信息*/
    public ApiResponse<?> updateDictData(SysDictData dictData) {
        Optional<SysDictData> dictDataOptional = dictDataRepository.findById(dictData.getDictCode());
        if (dictDataOptional.isPresent()) {
            SysDictData save = dictDataOptional.get();

            save.setDictLabel(dictData.getDictLabel());
            save.setDictType(dictData.getDictType());
            return ApiResponse.success("修改成功",save);
        }
        return ApiResponse.error("修改失败",null);
    }

    /**
     * TODO 修改为假删
     * @param ids
     * @return
     */
    /*批量删除字典数据*/
    public ApiResponse<?> deleteDictData(List<Long> ids) {
        List<SysDictData> allById = dictDataRepository.findAllById(ids);
        if (allById.isEmpty()) {
            return ApiResponse.error();
        }
        dictDataRepository.deleteAll(allById);
        return ApiResponse.success("删除成功");
    }
}
