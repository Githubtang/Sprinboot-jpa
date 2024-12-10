package com.example.springboottyy.service;

import com.example.springboottyy.model.SysLog;
import com.example.springboottyy.repository.SysLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/11/13 17:20
 * @Version: 1.0
 */
@Service
public class LogService {

    @Autowired
    private SysLogRepository logRepository;

    public List<SysLog> searchByLevel(String level) {
        return logRepository.findByLevel(level);
    }

    public List<SysLog> searchByMessage(String keyword) {
        return logRepository.findByMessageContaining(keyword);
    }

    public List<SysLog> searchByTimeRange(String beginTime, String endTime) {
        return logRepository.findByTimestampBetween(beginTime, endTime);
    }
}
