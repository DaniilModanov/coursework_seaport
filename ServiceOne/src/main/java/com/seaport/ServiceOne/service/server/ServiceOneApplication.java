package com.seaport.ServiceOne.service.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ServiceOneApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOneApplication.class, args);
    }
}
