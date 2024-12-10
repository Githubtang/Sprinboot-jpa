package com.example.springboottyy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/11/25 18:21
 * @Version: 1.0
 */
@Service
public class KafkaProducerService {
    private static final String TOPIC = "log-topic";
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        log.info("Message sent : {} ", message);
        System.out.println("Message sent: " + message);
    }
}
