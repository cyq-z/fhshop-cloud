package com.fh.shop.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: ShopCartApp
 * @Author: cyq
 * @Date: 2021/6/8 19:43
 */
@SpringBootApplication
@EnableFeignClients
public class ShopCartApp {

    public static void main(String[] args) {
        SpringApplication.run(ShopCartApp.class, args);
    }
}
