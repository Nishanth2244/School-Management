package com.project.student.education.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class KeepAliveScheduler {

    @Autowired
    private RestTemplate restTemplate;

    private final String myUrl = "https://school-management-crba.onrender.com/api/driver/all";

    @Scheduled(fixedRate = 600000)
    public void keepAlive() {
        try {
            restTemplate.getForObject(myUrl, String.class);
            System.out.println("Keep-alive ping sent at " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Ping failed: " + e.getMessage());
        }
    }
}   