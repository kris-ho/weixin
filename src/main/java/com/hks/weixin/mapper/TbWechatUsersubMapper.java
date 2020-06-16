package com.hks.weixin.mapper;

import com.hks.weixin.pojo.TbWechatUsersub;
import com.hks.weixin.pojo.TbWechatUsersubExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbWechatUsersubMapper {
    int countByExample(TbWechatUsersubExample example);

    int deleteByExample(TbWechatUsersubExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbWechatUsersub record);

    int insertSelective(TbWechatUsersub record);

    List<TbWechatUsersub> selectByExample(TbWechatUsersubExample example);

    TbWechatUsersub selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbWechatUsersub record, @Param("example") TbWechatUsersubExample example);

    int updateByExample(@Param("record") TbWechatUsersub record, @Param("example") TbWechatUsersubExample example);

    int updateByPrimaryKeySelective(TbWechatUsersub record);

    int updateByPrimaryKey(TbWechatUsersub record);
}