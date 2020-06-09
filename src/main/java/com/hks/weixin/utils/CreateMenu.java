package com.hks.weixin.utils;

import com.hks.weixin.pojo.*;
import com.hks.weixin.service.WeChatService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateMenu {
    @Autowired
    private static WeChatService weChatServiceImpl;

    public static void main(String[] args) {
        //菜单对象
        Button btn = new Button();
        //第一个一级菜单
        btn.getButton().add(new ClickButton("一级点击", "1"));
        //第二个一级菜单
        btn.getButton().add(new ViewButton("一级跳转", "http://www.baidu.com"));
        //创建第三个一级菜单
        SubButton sb = new SubButton("有子菜单");
        //为第三个一级菜单增加子菜单
        sb.getSub_button().add(new PhotoOrAlbumButton("传图", "31"));
        sb.getSub_button().add(new ClickButton("点击", "32"));
        sb.getSub_button().add(new ViewButton("网易新闻", "http://news.163.com"));
        //加入第三个一级菜单
        btn.getButton().add(sb);
        //转为json
        JSONObject jsonObject = JSONObject.fromObject(btn);
        //准备url
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", weChatServiceImpl.getAccessToken());
        //发送请求
        String result = WxUtil.post(url, jsonObject.toString());
        System.out.println(result);
    }
}
