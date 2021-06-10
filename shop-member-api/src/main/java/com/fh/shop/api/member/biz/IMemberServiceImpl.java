package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.Constants;
import com.fh.shop.common.KeyUtil;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 崔亚倩
 */
@Service("memberService")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class IMemberServiceImpl implements IMemberService {

    @Resource
    private IMemberMapper memberMapper;

    @Override
    public ServerResponse login(String memberName, String password) {
        //非空判断
        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(password)) {
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_INFO_IS_NULL);

        }
        //获取数据库信息 验证用户是否存在
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("memberName", memberName);
        Member memberDB = memberMapper.selectOne(memberQueryWrapper);
        if (memberDB == null) {
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_MEMBER_NAME_IS_EXIST);
        }
        //判断用户是否激活，
        if (memberDB.getStatus() == 0) {
            Long id = memberDB.getId();
            String mail = memberDB.getMail();
            Map<String, String> result = new HashMap<>();
            result.put("id", id + "");
            result.put("mail", mail);
            return ServerResponse.error(ResponseEnum.MEMBER_STATUS_ERROR, result);
        }
        //对比密码是否正确
        if (!Md5Util.md5(password).equals(memberDB.getPassword())) {
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_PASSWORD_IS_ERROR);
        }
        //==========生成标签 返回到客户端
        MemberVo memberVo = new MemberVo();
        Long id = memberDB.getId();
        memberVo.setId(id);
        memberVo.setMemberName(memberDB.getMemberName());
        memberVo.setNickName(memberDB.getNickName());
        //对象转化为string类型
        String memberVoJson = JSON.toJSONString(memberVo);
        //生成sign标签
        String sign = Md5Util.sign(memberVoJson, Constants.SECRET);
        //对信息进行base64的转化
        String memberVoJsonBase64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        String signBase64 = Base64.getEncoder().encodeToString(sign.getBytes());
        //信息往redis里面存一份
        RedisUtil.setex(KeyUtil.buildMemberKey(id), "", Constants.EXISTS_TIME);
        //==========生成标签 返回到客户端
        return ServerResponse.success(memberVoJsonBase64 + "." + signBase64);
    }


}
