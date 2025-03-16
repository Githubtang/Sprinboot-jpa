package com.example.springboottyy.service;

import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.model.SysNotice;
import com.example.springboottyy.repository.SysNoticeRepository;
import com.example.springboottyy.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: GithubTang
 * @Description: 公告 服务层实现
 * @Date: 2025/3/16 20:45
 * @Version: 1.0
 */
@Service
public class SysNoticeService {
    @Autowired
    private SysNoticeRepository noticeRepository;

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public ApiResponse<?> selectNoticeById(Long noticeId) {
        Optional<SysNotice> byId = noticeRepository.findById(noticeId);
        if (byId.isPresent()) {
            return ApiResponse.success(byId.get());
        }
        return ApiResponse.error();
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    public ApiResponse<?> selectNoticeList(SysNotice notice) {
        return ApiResponse.success(noticeRepository.findAll());
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public ApiResponse<?> insertNotice(SysNotice notice) {
        try {
            noticeRepository.save(notice);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return ApiResponse.success();
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    public ApiResponse<?> updateNotice(SysNotice notice) {
        try {
            SysNotice oldNotice = noticeRepository.findById(notice.getId()).get();
            if (oldNotice.getId().equals(notice.getId())) {
                noticeRepository.save(notice);
                return ApiResponse.success();
            }
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        return ApiResponse.error();
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    public ApiResponse<?> deleteNoticeById(Long noticeId) {
        try {
            noticeRepository.deleteById(noticeId);
            return ApiResponse.success();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    public ApiResponse<?> deleteNoticeByIds(List<Long> noticeIds) {
        try {
            noticeRepository.deleteAllById(noticeIds);
            return ApiResponse.success();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
