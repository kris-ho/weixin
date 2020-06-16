package com.hks.weixin.service.impl;

import com.hks.weixin.mapper.TbWechatUsersubMapper;
import com.hks.weixin.pojo.TbWechatUser;
import com.hks.weixin.pojo.TbWechatUsersub;
import com.hks.weixin.service.UserSubInfoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserSubInfoDaoImpl implements UserSubInfoDao {
    @Resource
    private TbWechatUsersubMapper tbWechatUsersubMapperImpl;

    @Override
    public int insTbWechatUserSub(TbWechatUsersub tbWechatUsersub) {
        return tbWechatUsersubMapperImpl.insert(tbWechatUsersub);
    }
}
