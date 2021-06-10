package com.fh.shop.api.goods.biz;

import com.fh.shop.common.ServerResponse;

/**
 * @author love
 */
public interface ISkuService {

    ServerResponse findRecommendNewProduct();

    ServerResponse selectById(Long skuId);
}
