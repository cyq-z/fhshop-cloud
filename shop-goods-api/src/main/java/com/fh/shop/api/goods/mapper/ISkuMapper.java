package com.fh.shop.api.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.goods.po.Sku;

import java.util.List;

/**
 * @author love
 */
public interface ISkuMapper extends BaseMapper<Sku> {
    List<Sku> findRecommendNewProduct();


}
