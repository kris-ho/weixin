package com.hks.weixin.mapper;

import com.hks.weixin.pojo.TbWechatUser;
import com.hks.weixin.pojo.TbWechatUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface TbWechatUserMapper {
    @Select("select * from tb_wechat_user")
    List<TbWechatUser> findAll();

    int countByExample(TbWechatUserExample example);

    int deleteByExample(TbWechatUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbWechatUser record);

    int insertSelective(TbWechatUser record);

    List<TbWechatUser> selectByExample(TbWechatUserExample example);

    TbWechatUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbWechatUser record, @Param("example") TbWechatUserExample example);

    int updateByExample(@Param("record") TbWechatUser record, @Param("example") TbWechatUserExample example);

    int updateByPrimaryKeySelective(TbWechatUser record);

    int updateByPrimaryKey(TbWechatUser record);
}