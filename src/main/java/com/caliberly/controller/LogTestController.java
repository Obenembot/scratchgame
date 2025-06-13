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
        int value1 = 10;
        int value2 = 15;
        if (value1 > value2) {
            logger.info("Value 1 is greater than value 2");
        } else {
            logger.info("Value 2 is greater than value 1");
        }
        return "Log sent!";
    }
}
