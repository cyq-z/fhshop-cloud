package com.fh.shop.api.cart.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author love
 */
@Data
public class CartItemVo implements Serializable {

    private String image;

    private String skuName;

    private String price;

    private Long count;

    private Long skuId;

    private String subPrice;

}
