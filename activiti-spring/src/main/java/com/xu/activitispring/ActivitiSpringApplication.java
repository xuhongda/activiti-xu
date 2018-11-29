package com.xu.activitispring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:config/activiti.cfg.xml"})
public class ActivitiSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiSpringApplication.class, args);
    }
}
