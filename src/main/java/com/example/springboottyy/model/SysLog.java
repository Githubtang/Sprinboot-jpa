package com.example.springboottyy.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "app-logs-*")
public class SysLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String message;

    private String level;

    private String timestamp;
}
