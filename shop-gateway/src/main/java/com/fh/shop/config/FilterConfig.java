package com.fh.shop.config;

import com.fh.shop.filter.CrossFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: FilterConfig
 * @Author: cyq
 * @Date: 2021/6/8 13:51
 */
@Configuration
public class FilterConfig {

    @Bean
    public CrossFilter crossFilter() {
        return new CrossFilter();
    }
}
