package com.example.springboottyy.controller;

import com.example.springboottyy.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/11/27 17:01
 * @Version: 1.0
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @GetMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        kafkaProducerService.sendMessage(message);
        return "success";
    }
}
