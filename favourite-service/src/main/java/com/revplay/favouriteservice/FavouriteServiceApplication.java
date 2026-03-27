package com.revplay.favouriteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// Temporarily disable Eureka and Feign for standalone testing
// @EnableDiscoveryClient
// @EnableFeignClients(basePackages = "com.revplay.common.feign")
@ComponentScan(basePackages = {"com.revplay.favouriteservice"}) // Only scan local package
public class FavouriteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavouriteServiceApplication.class, args);
    }

}
