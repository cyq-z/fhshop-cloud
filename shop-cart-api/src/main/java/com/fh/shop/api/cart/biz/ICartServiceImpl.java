package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.cart.vo.CartItemVo;
import com.fh.shop.api.cart.vo.CartVo;
import com.fh.shop.api.goods.IGoodsFeignService;
import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.common.Constants;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.BigDecimalUtil;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author love
 */
@Service("cartService")
@Transactional(rollbackFor = Exception.class)
public class ICartServiceImpl implements ICartService {

    @Autowired
    private IGoodsFeignService goodsFeignService;

    @Value("${sku.count.limit}")
    private int countLimit;

    @Override
    public ServerResponse addCartItem(Long memberId, Long skuId, Long count) {
        //商品是否存在，
        ServerResponse<Sku> skuServerResponse = goodsFeignService.selectById(skuId);
        Sku sku = skuServerResponse.getData();
        if (sku == null) {
            return ServerResponse.error(ResponseEnum.CART_SKU_INFO_IS_ERROR);
        }
        // 商品是否上架
        String status = sku.getStatus();
        if (status.equals(Constants.STATUS_DOWN)) {
            return ServerResponse.error(ResponseEnum.CART_SKU_STATUS_IS_DOWN);
        }
        // 商品的库存是否大于等于count
        Integer stock = sku.getStock();
        if (stock <= count) {
            return ServerResponse.error(ResponseEnum.CART_SKU_STOCK_IS_ERROR);
        }
        //看用户是否有购物车
        String key = KeyUtil.buildCartKey(memberId);
        String cartVoJson = RedisUtil.hget(key, Constants.CART_INFO);
        //如果没有，就创建购物车，放值
        if (StringUtils.isEmpty(cartVoJson)) {
            //如果count<0,就提示信息不合法
            if (count <= 0) {
                return ServerResponse.error(ResponseEnum.CART_SKU_IS_ERROR);
            }
            CartVo cartVo = new CartVo();
            //组装cartItemVo
            extracted(skuId, count, sku, cartVo);
            //更新redis hash类型
            updateHmset(key, cartVo, count);
        } else {
            //如果有购物车
            CartVo cartVo = JSON.parseObject(cartVoJson, CartVo.class);
            List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
            //判断是否有这次加入购物车的商品信息
            Optional<CartItemVo> item = cartItemVoList.stream().filter(x -> x.getSkuId().longValue() == skuId.longValue()).findFirst();
            //如果存在
            if (item.isPresent()) {
                // 购物车有这款商品，找到这款商品，更新商品的数量，小计
                CartItemVo cartItemVo = item.get();
                long countNow = cartItemVo.getCount() + count;
                // 个数不能大于10,大于10的话,就提示信息
                if (countNow > countLimit) {
                    return ServerResponse.error(ResponseEnum.CART_SKU_COUNT_IS_NULL);
                }
                if (countNow <= 0) {
                    //移除数据
                    cartItemVoList.removeIf(x -> x.getSkuId().longValue() == cartItemVo.getSkuId().longValue());
                    //如果购物车数据都没有了，就需要删除购物车
                    if (cartItemVoList.size() == 0) {
                        RedisUtil.del(key);
                        return ServerResponse.success();
                    }
                    //更新redis
                    updateCartRedis(key, cartVo);
                    return ServerResponse.success();
                }
                cartItemVo.setCount(countNow);
                BigDecimal subPrice = new BigDecimal(cartItemVo.getSubPrice());
                String subPriceStr = subPrice.add(BigDecimalUtil.mul(cartItemVo.getPrice(), count + "")).toString();
                cartItemVo.setSubPrice(subPriceStr);
                // 更新购物车
                updateCartRedis(key, cartVo);
            } else {
                //如果count<0,就提示信息不合法
                if (count <= 0) {
                    return ServerResponse.error(ResponseEnum.CART_SKU_IS_ERROR);
                }
                //如果没有这个商品就直接加入购物车
                buildCartItemVo(skuId, count, sku, cartVo);
                //更新redis购物车
                updateCartRedis(key, cartVo);
            }
        }
        return ServerResponse.success();
    }

    private void extracted(Long skuId, Long count, Sku sku, CartVo cartVo) {
        buildCartItemVo(skuId, count, sku, cartVo);
        String price = null;
        cartVo.setTotalCount(count);
        cartVo.setTotalPrice(price);
    }

    private void buildCartItemVo(Long skuId, Long count, Sku sku, CartVo cartVo) {
        CartItemVo cartItemVo = new CartItemVo();
        cartItemVo.setCount(count);
        cartItemVo.setImage(sku.getImage());
        String price = sku.getPrice().toString();
        cartItemVo.setPrice(price);
        cartItemVo.setSkuId(skuId);
        cartItemVo.setSkuName(sku.getSkuName());
        String subPrice = BigDecimalUtil.mul(price, count + "").toString();
        cartItemVo.setSubPrice(subPrice);
        cartVo.getCartItemVoList().add(cartItemVo);
    }

    private void updateCartRedis(String key, CartVo cartVo) {
        // 更新购物车
        List<CartItemVo> cartItemVos = cartVo.getCartItemVoList();
        long totalCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemVo itemVo : cartItemVos) {
            totalCount += itemVo.getCount();
            totalPrice = totalPrice.add(new BigDecimal(itemVo.getSubPrice()));
        }
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        //更新redis hash类型
        updateHmset(key, cartVo, totalCount);
    }

    private void updateHmset(String key, CartVo cartVo, long totalCount) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.CART_INFO, JSON.toJSONString(cartVo));
        map.put(Constants.CART_COUNT, totalCount + "");
        RedisUtil.hmset(key, map);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findCart(Long memberId) {
        String key = KeyUtil.buildCartKey(memberId);
        String cartVoJson = RedisUtil.hget(key, Constants.CART_INFO);
        if (StringUtils.isEmpty(cartVoJson)) {
            return ServerResponse.error(ResponseEnum.CART_SKU_INFO_IS_NULL);
        }
        CartVo cartVo = JSON.parseObject(cartVoJson, CartVo.class);
        return ServerResponse.success(cartVo);
    }

    @Override
    public ServerResponse deleteCart(Long memberId, Long skuId) {
        //判断redis中是否有这个商品
        String key = KeyUtil.buildCartKey(memberId);
        String cartVoJson = RedisUtil.hget(key, Constants.CART_INFO);
        CartVo cartVo = JSON.parseObject(cartVoJson, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Optional<CartItemVo> first = cartItemVoList.stream().filter(x -> x.getSkuId().longValue() == skuId.longValue()).findFirst();
        if (!first.isPresent()) {
            return ServerResponse.error();
        }
        //删除
        cartItemVoList.removeIf(x -> x.getSkuId().longValue() == skuId.longValue());
        if (cartItemVoList.size() == 0) {
            RedisUtil.del(key);
            return ServerResponse.success();
        }
        //更新redis
        updateCartRedis(key, cartVo);
        return ServerResponse.success(ResponseEnum.CART_SKU_DELETE_SUCCESS);
    }

    @Override
    public ServerResponse deleteBatch(Long memberId, String skuIds) {
        if (StringUtils.isEmpty(skuIds)) {
            return ServerResponse.error();
        }
        String key = KeyUtil.buildCartKey(memberId);
        String cartVoJson = RedisUtil.hget(key, Constants.CART_INFO);
        CartVo cartVo = JSON.parseObject(cartVoJson, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Arrays.stream(skuIds.split(",")).forEach(x -> cartItemVoList.removeIf(y -> y.getSkuId().longValue() == Long.valueOf(x)));
        if (cartItemVoList.size() == 0) {
            RedisUtil.del(key);
            return ServerResponse.success();
        }
        updateCartRedis(key, cartVo);
        return ServerResponse.success();
    }


}
