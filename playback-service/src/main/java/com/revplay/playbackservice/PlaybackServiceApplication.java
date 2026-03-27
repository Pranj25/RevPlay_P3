package com.revplay.playbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PlaybackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaybackServiceApplication.class, args);
    }

}
