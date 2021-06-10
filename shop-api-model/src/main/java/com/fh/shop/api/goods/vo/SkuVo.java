package com.fh.shop.api.goods.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author love
 */
@Data
@ApiModel
public class SkuVo implements Serializable {

    @ApiModelProperty(value = "主键id",example = "0")
    private Long id;
    @ApiModelProperty(value = "sku名字")
    private String skuName;
    @ApiModelProperty(value = "sku价格")
    private String price;
    @ApiModelProperty(value = "sku图片")
    private String image;
}
