package com.hks.weixin.utils;

import com.baidu.aip.ocr.AipOcr;
import com.hks.weixin.pojo.BaseMessage;
import com.hks.weixin.pojo.Item;
import com.hks.weixin.pojo.NewsMessage;
import com.hks.weixin.pojo.TextMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.*;

public final class MsgTypeUtil {
    /**
     * 返回图灵机器人的回复消息
     *
     * @param msg
     * @return
     * @throws IOException
     */
    private static String chat(String msg, String apiKey, String apiUrl, String userID) throws IOException {
        return WxUtil.getAnswer(msg, apiKey, apiUrl, userID);
    }

    /**
     * 处理用户发来的文字消息
     *
     * @param requestMap
     * @param apiKey
     * @param apiUrl
     * @param userID
     * @param url
     * @return
     */
    public static BaseMessage dealTextMsg(Map<String, String> requestMap, String apiKey, String apiUrl, String userID, String url) {
        //用户发来的内容
        String msg = requestMap.get("Content");
        if (msg.equals("图文")) {
            List<Item> articles = new ArrayList<>();
            articles.add(new Item("这是图文消息的标题", "这是图文消息的详细介绍", "https://upload-images.jianshu.io/upload_images/23392391-7d66c1632558a51a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240", "http://www.baidu.com"));
            NewsMessage nm = new NewsMessage(requestMap, articles);
            return nm;
        }
        if (msg.equals("登录")) {
            TextMessage tm = new TextMessage(requestMap, "点击<a href=\"" + url + "\">这里</a>登录");
            return tm;
        }
        //调用方法返回聊天的内容
        String resp = null;
        try {
            resp = chat(msg, apiKey, apiUrl, userID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextMessage tmChat = new TextMessage(requestMap, resp);
        return tmChat;
    }

    /**
     * 处理用户发来的图片消息-图片识别文字
     *
     * @param requestMap
     * @param appBID
     * @param apiBKey
     * @param secretBKey
     * @return
     */
    public static BaseMessage dealImage(Map<String, String> requestMap, String appBID, String apiBKey, String secretBKey) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(appBID, apiBKey, secretBKey);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        String path = requestMap.get("PicUrl");

        //进行网络图片的识别
        org.json.JSONObject res = client.generalUrl(path, new HashMap<String, String>());
        String json = res.toString();
        //转为jsonObject
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        Iterator<JSONObject> it = jsonArray.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            JSONObject next = it.next();
            sb.append(next.getString("words"));
        }
        return new TextMessage(requestMap, sb.toString());
    }

    /**
     * 处理用户的view事件
     *
     * @param requestMap
     * @return
     */
    public static BaseMessage dealView(Map<String, String> requestMap) {
        return null;
    }

    /**
     * 处理用户的click事件
     *
     * @param requestMap
     * @return
     */
    public static BaseMessage dealClick(Map<String, String> requestMap, String url) {
        String key = requestMap.get("EventKey");
        switch (key) {
            //点击一菜单点
            case "1":
                //处理点击菜单
                return new TextMessage(requestMap, "你点了一点第一个一级菜单");
            case "32":
                //处理点击了第三个一级菜单的第二个子菜单
                return new TextMessage(requestMap, "点击<a href=\"" + url + "\">这里</a>登录");
            default:
                break;
        }
        return null;
    }

    public static String dealSubscribe(Map<String, String> requestMap) {
        return requestMap.get("FromUserName");
//        return new TextMessage(requestMap, "点击<a href=\"" + url + "\">这里</a>登录");
    }
}
