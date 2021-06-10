package com.fh.shop.api.member.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author love
 */
@Data
public class Member implements Serializable {

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
     * 会员密码.
     */
    private String password;

    /**
     * 会员手机号.
     */
    private String phone;

    /**
     * 邮箱.
     */
    private String mail;

    /**
     * 会员的激活状态.
     */
    private Integer status;

    /**
     * 会员的积分.
     */
    private Long score;
}