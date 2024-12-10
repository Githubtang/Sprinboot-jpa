package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysLog;
import com.example.springboottyy.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log")
    public String logMessage() {
        log.info("logMessage");
        return "logMessage";
    }

    @GetMapping("/level/{level}")
    public List<SysLog> getLogsByLevel(@PathVariable String level) {
        return logService.searchByLevel(level);
    }

    @GetMapping("/message/{keyword}")
    public List<SysLog> getLogsByMessage(@PathVariable String keyword) {
        return logService.searchByMessage(keyword);
    }

    @GetMapping("/time")
    public List<SysLog> getLogsByTimeRange(@RequestParam String beginTime, @RequestParam String endTime) {
        return logService.searchByTimeRange(beginTime, endTime);
    }

}
