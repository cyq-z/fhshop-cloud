package com.fh.shop.api.goods.biz;

import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.common.ServerResponse;

public interface ISkuService {

    ServerResponse findRecommendNewProduct();

    ServerResponse selectById(Long skuId);
}
