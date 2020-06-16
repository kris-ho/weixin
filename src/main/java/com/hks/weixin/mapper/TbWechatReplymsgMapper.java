package com.hks.weixin.mapper;

import com.hks.weixin.pojo.TbWechatReplymsg;
import com.hks.weixin.pojo.TbWechatReplymsgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbWechatReplymsgMapper {
    int countByExample(TbWechatReplymsgExample example);

    int deleteByExample(TbWechatReplymsgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbWechatReplymsg record);

    int insertSelective(TbWechatReplymsg record);

    List<TbWechatReplymsg> selectByExample(TbWechatReplymsgExample example);

    TbWechatReplymsg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbWechatReplymsg record, @Param("example") TbWechatReplymsgExample example);

    int updateByExample(@Param("record") TbWechatReplymsg record, @Param("example") TbWechatReplymsgExample example);

    int updateByPrimaryKeySelective(TbWechatReplymsg record);

    int updateByPrimaryKey(TbWechatReplymsg record);
}