package com.example.springboottyy.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/11/25 18:22
 * @Version: 1.0
 */
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "log-topic", groupId = "my-group")
    public void consumeMessage(String message) {
        System.out.println("Message received: " + message);
    }
}
