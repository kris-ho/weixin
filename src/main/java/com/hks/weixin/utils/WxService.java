package com.hks.weixin.utils;

import com.hks.weixin.pojo.*;
import com.thoughtworks.xstream.XStream;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WxService {
    /*图灵机器人*/
    //存储APIkey
    private static final String APIKEY = "09e963e9cd4e44fd810f27fb7f31cd58";
    //存储接口请求地址
    private static final String APIURL = "http://openapi.tuling123.com/openapi/api/v2";
    // 用户id
    private static final String USERID = "382599";

    /*获取AccessToken*/
    private static final String GET_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //微信公众号
    private static final String APPID="wxbeb920e4bccc46e0";
    private static final String APPSECRET="8f841e7044ef26d225c3f8707b356d33";

    //用于存储token
    private static AccessToken at;

    public static boolean check(String token, String timestamp, String nonce, String signature) {
        //1）将token、timestamp、nonce三个参数进行字典序排序
        String[] strs = new String[]{token, timestamp, nonce};
        Arrays.sort(strs);
        //2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0] + strs[1] + strs[2];
        String mysig = sha1(str);//可以不自己加sha1()
        //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (mysig != null) {
            return mysig.equalsIgnoreCase(signature);
        }
        return false;
    }

    public static String sha1(String str) {

        MessageDigest md = null;
        try {
            //获取一个加密对象
            md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(str.getBytes());

            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            //处理加密结果
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> parseRequest(InputStream is) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            //读取输入流 获取文档对象
            Document document = reader.read(is);
            //根据文档对象获取根节点
            Element root = document.getRootElement();
            //获取根节点的所有子节点
            List<Element> elements = root.elements();
            //遍历
            for (Element e : elements) {
                map.put(e.getName(), e.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 用于处理所有的事件和消息回复
     *
     * @param requestMap
     * @return xml数据
     */
    public static String getRespose(Map<String, String> requestMap) {
        BaseMessage msg = null;
        String msgType = requestMap.get("MsgType");
        switch (msgType) {
            case "text":
                msg = dealTextMessage(requestMap);
                break;
            case "image":

                break;
            case "voice":

                break;
            case "video":

                break;
            case "shortvideo":

                break;
            case "location":

                break;
            case "link":

                break;
            case "event":

                break;
            default:
                break;
        }
        if (msg != null) {
            return beanToXml(msg);
        }
        return null;
    }

    private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
        //用户发来的内容
        String msg = requestMap.get("Content");
        if (msg.equals("图文")) {
            List<Item> articles = new ArrayList<>();
            articles.add(new Item("这是图文消息的标题", "这是图文消息的详细介绍", "http://mmbiz.qpic.cn/mmbiz_jpg/dtRJz5K066YczqeHmWFZSPINM5evWoEvW21VZcLzAtkCjGQunCicDubN3v9JCgaibKaK0qGrZp3nXKMYgLQq3M6g/0", "http://www.baidu.com"));
            NewsMessage nm = new NewsMessage(requestMap, articles);
            return nm;
        }
        if (msg.equals("登录")) {
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb6777fffdf5b64a4&redirect_uri=http://www.6sdd.com/weixin/GetUserInfo&response_type=code&scope=snsapi_userinfo#wechat_redirect";
            TextMessage tm = new TextMessage(requestMap, "点击<a href=\"" + url + "\">这里</a>登录");
            return tm;
        }
        //调用方法返回聊天的内容
        String resp = null;
        try {
            resp = chat(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextMessage tm = new TextMessage(requestMap, resp);
        return tm;
    }

    private static String chat(String msg) throws IOException {
        return WxUtil.getAnswer(msg, APIKEY, APIURL, USERID);
    }

    /**
     * 把消息对象处理为xml对象
     *
     * @param msg
     * @return
     */
    private static String beanToXml(BaseMessage msg) {
        XStream stream = new XStream();
        //设置需要处理XStreamAlias("xml")注释的类
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMessage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VideoMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        return stream.toXML(msg);
    }

    /**
     * 获取token
     */
    private static void getToken() {
        String url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        String tokenStr = WxUtil.get(url);
        JSONObject jsonObject = JSONObject.fromObject(tokenStr);
        String token = jsonObject.getString("access_token");
        String expireIn = jsonObject.getString("expires_in");
        //创建token对象,并存起来。
        at = new AccessToken(token, expireIn);
    }

    /**
     * 向外暴露的获取token的方法
     * @return
     */
    public static String getAccessToken() {
        if(at==null||at.isExpired()) {
            getToken();
        }
        return at.getAccessToken();
    }

}
