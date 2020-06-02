package com.hks.weixin.utils;

import com.hks.weixin.pojo.*;
import com.sun.org.apache.xerces.internal.xs.XSTerm;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WxService {

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
        if (msg != null){
            return  beanToXml(msg);
        }
        return  null;
    }

    private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
        //用户发来的内容
        String msg = requestMap.get("Content");
        if(msg.equals("图文")) {
            List<Item> articles = new ArrayList<>();
            articles.add(new Item("这是图文消息的标题", "这是图文消息的详细介绍", "http://mmbiz.qpic.cn/mmbiz_jpg/dtRJz5K066YczqeHmWFZSPINM5evWoEvW21VZcLzAtkCjGQunCicDubN3v9JCgaibKaK0qGrZp3nXKMYgLQq3M6g/0", "http://www.baidu.com"));
            NewsMessage nm = new NewsMessage(requestMap, articles);
            return nm;
        }
        if(msg.equals("登录")) {
            String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb6777fffdf5b64a4&redirect_uri=http://www.6sdd.com/weixin/GetUserInfo&response_type=code&scope=snsapi_userinfo#wechat_redirect";
            TextMessage tm = new TextMessage(requestMap, "点击<a href=\""+url+"\">这里</a>登录");
            return tm;
        }
        //调用方法返回聊天的内容
        //String resp = chat(msg);
//        TextMessage tm = new TextMessage(requestMap, resp);
        TextMessage tm = new TextMessage(requestMap, "你要干啥");
        return tm;
    }

//    private static String chat(String msg) {
//        String result =null;
//        String url ="http://op.juhe.cn/robot/index";//请求接口地址
//        Map params = new HashMap();//请求参数
//        params.put("key",APPKEY);//您申请到的本接口专用的APPKEY
//        params.put("info",msg);//要发送给机器人的内容，不要超过30个字符
//        params.put("dtype","");//返回的数据的格式，json或xml，默认为json
//        params.put("loc","");//地点，如北京中关村
//        params.put("lon","");//经度，东经116.234632（小数点后保留6位），需要写为116234632
//        params.put("lat","");//纬度，北纬40.234632（小数点后保留6位），需要写为40234632
//        params.put("userid","");//1~32位，此userid针对您自己的每一个用户，用于上下文的关联
//        try {
//            result =Util.net(url, params, "GET");
//            //解析json
//            JSONObject jsonObject = JSONObject.fromObject(result);
//            //取出error_code
//            int code = jsonObject.getInt("error_code");
//            if(code!=0) {
//                return null;
//            }
//            //取出返回的消息的内容
//            String resp = jsonObject.getJSONObject("result").getString("text");
//            return resp;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 把消息对象处理为xml对象
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
}
