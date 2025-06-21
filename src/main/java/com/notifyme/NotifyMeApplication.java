package com.notifyme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class NotifyMeApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(NotifyMeApplication.class, args);
    }

    @PostConstruct
    public void logProfile() {
        System.out.println(">>> ACTIVE PROFILE: " + String.join(",", env.getActiveProfiles()));
    }
}

