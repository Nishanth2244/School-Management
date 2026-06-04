package com.project.student.education;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@EnableScheduling
@SpringBootApplication
public class EducationManagementApplication {

    public static void main(String[] args) {

        SpringApplication.run(EducationManagementApplication.class, args);
    }

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
