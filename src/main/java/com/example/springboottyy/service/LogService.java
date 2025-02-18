package com.example.springboottyy.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.springboottyy.model.SysLog;
import com.example.springboottyy.repository.SysLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Insight
 * @Description: TODO
 * @timestamp: 2024/11/13 17:20
 * @Version: 1.0
 */
@Service
public class LogService {

    @Autowired
    private SysLogRepository logRepository;

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private ElasticsearchTemplate template;
    @Qualifier("elasticsearchClient")
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<SysLog> searchByLevel(String level) {
        return logRepository.findByLevel(level);
    }

    public List<SysLog> searchByMessage(String keyword) {
        return logRepository.findByMessageContaining(keyword);
    }

    public List<SysLog> getLogsTimestampRange(String beginTime, String endTime) {
        return logRepository.findByTimestampBetween(beginTime, endTime);
    }

    public List<SysLog> getAppLogsIndices(){
        // 构建查询条件
        return null;
    }

}
