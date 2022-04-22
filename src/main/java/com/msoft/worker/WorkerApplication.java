package com.msoft.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableMongoAuditing
@EnableWebFlux
@SpringBootApplication
public class WorkerApplication {
    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        System.setProperty("appId", "worker");
        try {
            applicationContext = SpringApplication.run(WorkerApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
