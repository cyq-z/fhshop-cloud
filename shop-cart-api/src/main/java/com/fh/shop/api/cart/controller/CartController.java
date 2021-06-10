package com.fh.shop.api.cart.controller;

import com.fh.shop.api.GetMemberInfo;
import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author love
 */
@RestController
@RequestMapping("/api/cart")
@Api(tags = "购物车接口")
public class CartController extends GetMemberInfo {

    @Resource(name = "cartService")
    private ICartService cartService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/addCartItem")
    @ApiOperation(value = "添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头信息", dataType = "java.lang.String", paramType = "header", required = true),
            @ApiImplicitParam(name = "skuId", value = "sku的id", dataType = "java.lang.Long", required = true, example = "0"),
            @ApiImplicitParam(name = "count", value = "个数", dataType = "java.lang.Long", required = true, example = "0")
    })
    /**
     * @Description: 方法是
     * @param: skuId
    count
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:51
     */
    public ServerResponse addCartItem(Long skuId, Long count) {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.addCartItem(memberId, skuId, count);
    }

    @GetMapping("/findCart")
    @ApiOperation(value = "查询购物车里的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头信息", dataType = "java.lang.String", paramType = "header", required = true)
    })
    /**
     * @Description: 方法是
     * @param:
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:51
     */
    public ServerResponse findCart() {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.findCart(memberId);
    }

    @DeleteMapping("/deleteCart")
    @ApiOperation(value = "删除购物车里的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头信息", dataType = "java.lang.String", paramType = "header", required = true),
            @ApiImplicitParam(name = "skuId", value = "sku的id", dataType = "java.lang.Long", required = true, example = "0")
    })
    /**
     * @Description: 方法是
     * @param: skuId
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:51
     */
    public ServerResponse deleteCart(Long skuId) {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteCart(memberId, skuId);
    }


    @DeleteMapping("/deleteBatch")
    @ApiOperation(value = "批量删除购物车里的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头信息", dataType = "java.lang.String", paramType = "header", required = true),
            @ApiImplicitParam(name = "skuIds", value = "sku的id", dataType = "java.lang.String", required = true)
    })
    /**
     * @Description: 方法是
     * @param: skuIds
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:51
     */
    public ServerResponse deleteBatch(String skuIds) {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteBatch(memberId, skuIds);
    }
}
