package com.fh.shop.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DocumentationConfig
 * @Author: cyq
 * @Date: 2021/6/9 19:55
 */
@Component
@Primary
public class DocumentationConfig  implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        //api/cates/是网关路由，/v2/api-docs是swagger中的
        resources.add(swaggerResource("shop-cate-api", "/cate/v2/api-docs", "1.0"));
        resources.add(swaggerResource("shop-goods-api", "/goods/v2/api-docs", "1.0"));
        resources.add(swaggerResource("shop-member-api", "/member/v2/api-docs", "1.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
