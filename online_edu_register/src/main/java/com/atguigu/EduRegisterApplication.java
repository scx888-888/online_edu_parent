package com.atguigu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EduRegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduRegisterApplication.class, args);
    }

}