package com.fh.shop.api;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.Constants;
import com.fh.shop.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @ClassName: GetMemberInfo
 * @Author: cyq
 * @Date: 2021/6/9 14:12
 */
public class GetMemberInfo {

    public MemberVo buildMemberVo(HttpServletRequest request){
        try {
            String decode = URLDecoder.decode(request.getHeader(Constants.MEMBER_INFO), "utf-8");
            MemberVo memberVo = JSON.parseObject(decode, MemberVo.class);
            return memberVo;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
