package com.revplay.favouriteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FavouriteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavouriteServiceApplication.class, args);
    }

}
