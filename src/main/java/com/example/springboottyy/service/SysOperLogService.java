package com.example.springboottyy.service;

import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.SysOperLog;
import com.example.springboottyy.repository.SysOperLogRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: GithubTang
 * @Description: 操作日志 服务层处理
 * @Date: 2025/3/15 16:15
 * @Version: 1.0
 */
@Service
public class SysOperLogService {
    @Autowired
    private SysOperLogRepository repository;
    /**
     * 新增操作日志
     *
     * @param sysOperLog 操作日志对象
     */
    public void save(SysOperLog sysOperLog) {
        repository.save(sysOperLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param sysOperLog 操作日志对象
     * @return 操作日志集合
     */
    public ApiResponse<?> selectOperLogList(SysOperLog sysOperLog) {
        List<SysOperLog> repositoryAll = repository.findAll();
        return ApiResponse.success(repositoryAll);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    public void deleteOperLogByIds(List<Long> ids) {
        try {
            repository.deleteAllById(ids);
        }
        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 查询操作日志详细
     *
     * @param id 操作ID
     * @return 操作日志对象
     */
    public ApiResponse<?> selectOperLogById(Long id) {
        return ApiResponse.success(repository.findById(id).get());
    }

    /**
     * 清空操作日志
     */
    public void cleanOperLog(){
        try {
            repository.deleteAll();
        }
        catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

}
