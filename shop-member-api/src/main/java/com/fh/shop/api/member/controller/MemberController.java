package com.fh.shop.api.member.controller;

import com.fh.shop.api.GetMemberInfo;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.common.Constants;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import com.fh.shop.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author love
 */
@RestController
@Api(tags = "会员信息管理")
@RequestMapping("/api")
@Slf4j
public class MemberController extends GetMemberInfo {

    @Resource
    private IMemberService memberService;

    @Autowired
    private HttpServletRequest request;

    
    @PostMapping(value = "/member/login")
    @ApiOperation( "会员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberName",value = "会员名",dataType = "java.lang.String",required = true),
            @ApiImplicitParam(name = "password",value = "密码",dataType = "java.lang.String",required = true)
    })
    /**
     * @Description: 方法是
     * @param: memberName
    password
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:50
     */
    public ServerResponse login(String memberName, String password) {
        return memberService.login(memberName, password);
    }

    @GetMapping(value = "/member/findMember")
    @ApiOperation("会员登录成功之后返回会员的信息")
    @ApiImplicitParam(name = "x-auth",value = "头信息",required = true,paramType = "header")
    /**
     * @Description: 方法是
     * @param: 
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:50
     */
    public ServerResponse findMember()  {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        //查询购物车的count
        String key = KeyUtil.buildCartKey(memberId);
        String totalCountDB = RedisUtil.hget(key, Constants.CART_COUNT);
        if (StringUtils.isEmpty(totalCountDB)) {
            memberVo.setCount(0);
        } else {
            memberVo.setCount(Integer.parseInt(totalCountDB));
        }
        return ServerResponse.success(memberVo);
    }

    //注销
    @GetMapping(value = "/member/logout")
    @ApiOperation( "会员注销")
    /**
     * @Description: 方法是
     * @param: 
     * @return: com.fh.shop.common.ServerResponse
     * @auther: cyq
     * @date: 2021/06/09 22:50
     */
    public ServerResponse logout()  {
        MemberVo memberVo = buildMemberVo(request);
        RedisUtil.del(KeyUtil.buildMemberKey(memberVo.getId()));
        return ServerResponse.success();
    }


}
