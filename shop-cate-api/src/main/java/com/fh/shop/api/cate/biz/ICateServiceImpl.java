package com.fh.shop.api.cate.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.cate.mapper.ICateMapper;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author love
 */
@Service("cateService")
@Transactional(rollbackFor = Exception.class)
public class ICateServiceImpl implements ICateService {

    @Resource
    private ICateMapper cateMapper;

    @Override
    @Transactional(readOnly = true)
    public ServerResponse findCate() {
        //先从缓存中查询
        String cateListInfo = RedisUtil.get("cateList");
        //查询有数据，进行转换，返回list
        if (StringUtils.isNotEmpty(cateListInfo)){
            List<Cate> cateList = JSON.parseArray(cateListInfo, Cate.class);
            return ServerResponse.success(cateList);
        }
        //如果没有数据，就先从数据库查询
        List<Cate> cateList = cateMapper.selectList(null);
        //把list转为string[json格式]
        String cateListJson = JSON.toJSONString(cateList);
        //保存到缓存中
        RedisUtil.set("cateList",cateListJson);
        return ServerResponse.success(cateList);
    }
}
