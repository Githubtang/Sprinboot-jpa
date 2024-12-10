package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SysLogRepository extends ElasticsearchRepository<SysLog, String> {
    List<SysLog> findByLevel(String level);

    List<SysLog> findByMessageContaining(String keyword);

    List<SysLog> findByTimestampBetween(String startTime, String endTime);

}
