package com.example.springboottyy.service;

import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.SysLogininfor;
import com.example.springboottyy.repository.SysLogininforRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: GithubTang
 * @Description: 系统访问记录表 service
 * @Date: 2025/3/16 16:39
 * @Version: 1.0
 */
@Service
public class SysLogininforService {
    @Autowired
    private SysLogininforRepository logininforRepository;

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    public ApiResponse<?> insertLogininfor(SysLogininfor logininfor) {
        SysLogininfor save = logininforRepository.save(logininfor);
        return ApiResponse.success(save);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public ApiResponse<?> selectLogininforList(SysLogininfor logininfor) {
        return ApiResponse.success(logininforRepository.findAll());
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    public ApiResponse<?> deleteLogininfor(List<Long> infoIds) {
        try {
            logininforRepository.deleteAllById(infoIds);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return ApiResponse.success();
    }

    /**
     * 清空系统登录日志
     */
    public ApiResponse<?> cleanLogininfor() {
        try {
            logininforRepository.deleteAll();
        }
        catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return ApiResponse.success();
    }
}
