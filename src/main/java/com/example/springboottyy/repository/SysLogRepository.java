package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysLog;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SysLogRepository extends ElasticsearchRepository<SysLog, String> {
    List<SysLog> findByLevel(String level);

    List<SysLog> findByMessageContaining(String keyword);

    @Query("""
        {
          "range": {
            "@timestamp": {
              "gte": "?0",
              "lte": "?1"
            }
          }
        }
        """)
    List<SysLog> findByTimestampBetween(String beginTime, String endTime);

}
