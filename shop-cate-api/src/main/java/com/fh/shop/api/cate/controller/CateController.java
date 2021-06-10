package com.fh.shop.api.cate.controller;

import com.fh.shop.api.cate.biz.ICateService;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author love
 */
@RestController
@Api(tags = "分类信息管理")
@RequestMapping("/api")
@Slf4j
public class CateController {

    @Resource(name = "cateService")
    private ICateService cateService;
    @Value("${server.port}")
    private String port;


    /**
     * @Description: 方法是
     * @param: null
     * @return: 
     * @auther: cyq
     * @date: 2021/06/09 22:51
     */
    @RequestMapping(value = "/cate/findCate", method = RequestMethod.GET)
    @ApiOperation(value = "获取分类信息")
    public ServerResponse findCate() {
        log.info("端口号信息:{}", port);
        return cateService.findCate();
    }
}
