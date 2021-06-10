package com.fh.shop.api.goods.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.goods.mapper.ISkuMapper;
import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.api.goods.vo.SkuVo;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cyq
 */
@Service("skuService")
@Transactional(rollbackFor = Exception.class)
public class ISkuServiceImpl implements ISkuService {

    @Resource
    private ISkuMapper skuMapper;

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findRecommendNewProduct() {
        //先从缓存中查询
        String skuVoListInfo = RedisUtil.get("skuVoList");
        if (StringUtils.isNotEmpty(skuVoListInfo)) {
            List<SkuVo> skuVoList = JSON.parseArray(skuVoListInfo, SkuVo.class);
            return ServerResponse.success(skuVoList);
        }
        List<Sku> skuList = skuMapper.findRecommendNewProduct();
        List<SkuVo> skuVoList = skuList.stream().map(x -> {
            SkuVo skuVo = new SkuVo();
            skuVo.setId(x.getId());
            skuVo.setSkuName(x.getSkuName());
            skuVo.setImage(x.getImage());
            skuVo.setPrice(x.getPrice().toString());
            return skuVo;
        }).collect(Collectors.toList());
        String skuVoListJson = JSON.toJSONString(skuVoList);
        RedisUtil.setex(Constants.BUILD_LOGIN_MEMBER, skuVoListJson, 20);
        return ServerResponse.success(skuVoList);
    }

    @Override
    public ServerResponse selectById(Long skuId) {
        Sku sku = skuMapper.selectById(skuId);
        return ServerResponse.success(sku);
    }


}
