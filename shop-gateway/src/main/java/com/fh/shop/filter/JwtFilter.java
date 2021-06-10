package com.fh.shop.filter;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.Constants;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import com.fh.shop.vo.MemberVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName: JwtFilter
 * @Author: cyq
 * @Date: 2021/6/8 14:06
 */
@Component
@Slf4j
public class JwtFilter extends ZuulFilter {

    @Value("${fh.shop.checkUrls}")
    private List<String> checkUrls;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        log.info(String.valueOf(checkUrls));
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        //是否需要验签，这里我设置的url是需要验签的
        boolean isCheck = false;
        //把请求对比，如果不满足任何一个就进行验签 否则就
        for (String checkUrl : checkUrls) {
            if (requestURL.indexOf(checkUrl) > 0) {
                isCheck = true;
                break;
            }
        }
        if (!isCheck) {
            return null;
        }
        //验证
        String requestHeader = request.getHeader("x-auth");
        if (StringUtils.isEmpty(requestHeader)) {
            //不往微服务里发请求
            return buildReturnInfo(ResponseEnum.TOKEN_IS_MISS);
        }
        //判断对应信息格式是否正确
        String[] headerArr = requestHeader.split("\\.");
        if (headerArr.length != 2) {
            return buildReturnInfo(ResponseEnum.TOKEN_INFO_IS_ERROR);
        }
        //编码格式转换 验签
        String memberVoJsonBase64 = headerArr[0];
        String signBase64 = headerArr[1];
        String memberVoJson = null;
        String sign = null;
        memberVoJson = new String(Base64.getDecoder().decode(memberVoJsonBase64), "utf-8");
        sign = new String(Base64.getDecoder().decode(signBase64), "utf-8");
        String newSign = Md5Util.sign(memberVoJson, Constants.SECRET);
        if (!newSign.equals(sign)) {
            return buildReturnInfo(ResponseEnum.TOKEN_IS_ERROR);
        }
        //判断redis中信息是否存在
        MemberVo memberVo = JSON.parseObject(memberVoJson, MemberVo.class);
        Long id = memberVo.getId();
        if (!RedisUtil.exists(KeyUtil.buildMemberKey(id))) {
            return buildReturnInfo(ResponseEnum.TOKEN_LOGIN_ERROR);
        }
        //续命
        RedisUtil.expire(KeyUtil.buildMemberKey(id), Constants.EXISTS_TIME);
        //把信息放在request的header中
        currentContext.addZuulRequestHeader(Constants.MEMBER_INFO, URLEncoder.encode(memberVoJson,"utf-8"));
        return null;
    }

    private Object buildReturnInfo(ResponseEnum responseEnum) {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        //不往微服务里发请求 进行拦截
        currentContext.setSendZuulResponse(false);
        ServerResponse error = ServerResponse.error(responseEnum);
        String jsonString = JSON.toJSONString(error);
        currentContext.setResponseBody(jsonString);
        return null;
    }
}
