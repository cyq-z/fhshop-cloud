package com.fh.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * @author cyq
 */
@SpringBootApplication
@EnableEurekaServer
public class ShopRegCenterApp {

    public static void main(String[] args) {
        System.out.println("**********");
        SpringApplication.run(ShopRegCenterApp.class,args);
    }
}
