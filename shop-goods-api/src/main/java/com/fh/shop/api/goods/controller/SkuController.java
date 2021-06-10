package com.fh.shop.api.goods.controller;

import com.fh.shop.api.goods.biz.ISkuService;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cyq
 */
@RestController
@Api(tags = "sku信息管理")
@RequestMapping("/api")
public class SkuController {

    @Resource(name = "skuService")
    private ISkuService skuService;


    /**
     * @Description: 方法是
     * @param: 
     * @param skuId
     * @return: ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:46
     */
    @GetMapping("/selectById")
    public ServerResponse selectById(@RequestParam("skuId") Long skuId){
        return skuService.selectById(skuId);
    };


    
    @GetMapping("/spu/newProduct")
    @ApiOperation(value = "获取sku列表")
    public ServerResponse findList(){
      return skuService.findRecommendNewProduct();
    }

}
