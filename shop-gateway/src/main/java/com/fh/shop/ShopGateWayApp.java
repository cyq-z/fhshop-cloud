package com.fh.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author love
 */
@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class ShopGateWayApp {

    public static void main(String[] args) {
        SpringApplication.run(ShopGateWayApp.class,args);
    }
}
