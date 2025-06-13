package com.caliberly.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogTestController {

    private static final Logger logger = LoggerFactory.getLogger(LogTestController.class);

    @GetMapping("/test-log")
    public String logTest() {
        logger.info("This is an info log message sent to Graylog!");
        logger.error("This is an error log message!");
        return "Log sent!";
    }
}
