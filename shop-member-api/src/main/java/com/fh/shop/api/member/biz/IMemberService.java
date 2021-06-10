package com.fh.shop.api.member.biz;

import com.fh.shop.common.ServerResponse;

/**
 * @author love
 */
public interface IMemberService {


    ServerResponse login(String memberName, String password);

}
