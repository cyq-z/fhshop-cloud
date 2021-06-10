package com.fh.shop.api.cart.biz;

import com.fh.shop.common.ServerResponse;

/**
 * @author love
 */
public interface ICartService {

    ServerResponse addCartItem(Long memberId, Long skuId, Long count);

    ServerResponse findCart(Long memberId);

    ServerResponse deleteCart(Long memberId, Long skuId);

    ServerResponse deleteBatch(Long memberId, String skuIds);
}
