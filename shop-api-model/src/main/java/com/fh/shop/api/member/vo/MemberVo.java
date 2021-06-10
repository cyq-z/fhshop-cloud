package com.fh.shop.api.member.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cyq
 */
@Data
public class MemberVo implements Serializable {
    
    /**
     * 会员id.
     */
    private Long id;
    /**
     * 会员名.
     */
    private String memberName;

    /**
     * 会员昵称.
     */
    private String nickName;

    /**
     * 购物车的总个数.
     */
    private int count;
}
