package com.fh.shop.api.goods.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author love
 */
@Data
public class Sku implements Serializable {

    /**
     * skuId.
     */
    private Long id;
    /**
     * sku名字.
     */
    private String skuName;
    /**
     * 外键spuID.
     */
    private Long spuId;
    /**
     * sku价格.
     */
    private BigDecimal price;
    /**
     * sku的库存.
     */
    private Integer stock;
    /**
     * 相关的规格信息.
     */
    private String specInfo;
    /**
     * 关联的颜色.
     */
    private Long colorId;
    private String image;
    private String status;
    private String newProduct;
    private String recommend;
    private Integer saleCount;
}
