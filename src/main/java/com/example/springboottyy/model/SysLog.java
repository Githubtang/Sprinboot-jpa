package com.example.springboottyy.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Builder
@Document(indexName = "app-logs-*")
public class SysLog {
    @Id
    private String id;

    private String message;

    private String level;

    @Field(name = "@timestamp")
    private String timestamp;

    @Field(name = "@version")
    private String version;

}
