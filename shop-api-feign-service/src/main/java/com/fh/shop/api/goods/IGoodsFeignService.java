package com.fh.shop.api.goods;

import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.common.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: IGoodsFeignService
 * @Author: cyq
 * @Date: 2021/6/9 13:58
 */
@FeignClient(name = "shop-goods-api")
public interface IGoodsFeignService {
    /**
     *
     * @param skuId
     * @return
     */
    @GetMapping("/api/selectById")
    ServerResponse<Sku> selectById(@RequestParam("skuId") Long skuId);
}
