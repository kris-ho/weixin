package com.hks.weixin.service.impl;

import com.hks.weixin.service.UserInfoDao;
import com.hks.weixin.mapper.TbWechatUserMapper;
import com.hks.weixin.pojo.TbWechatUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoDaoImpl implements UserInfoDao {
    @Resource
    private TbWechatUserMapper tbWechatUserMapperImpl;

    @Override
    public int insTbWechatUser(TbWechatUser tbWechatUser) {
        return tbWechatUserMapperImpl.insert(tbWechatUser);
    }
}
