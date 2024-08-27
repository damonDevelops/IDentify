package com.team.identify.IdentifyAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IdentifyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdentifyApiApplication.class, args);
    }
}
