package com.hks.weixin.service.impl;

import com.hks.weixin.pojo.*;
import com.hks.weixin.service.UserInfoDao;
import com.hks.weixin.service.UserSubInfoDao;
import com.hks.weixin.service.WeChatService;
import com.hks.weixin.utils.MsgTypeUtil;
import com.hks.weixin.utils.WxUtil;
import com.thoughtworks.xstream.XStream;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Lang;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeChatServiceImpl implements WeChatService {
    @Value("${wechat.token}")
    private String token;

    //图灵机器人
    @Value("${tulin.apikey}")
    private String apiKey;
    @Value("${tulin.apiurl}")
    private String apiUrl;
    @Value("${tulin.userid}")
    private String userID;

    //微信公众号
    @Value("${wechat.tokenurl}")
    private String tokenUrl;
    @Value("${wechat.appid}")
    private String appID;
    @Value("${wechat.appsecret}")
    private String appSecret;
    @Value("${wechat.uurl}")
    private String uurl;

    //微信网页授权
    @Value("${wechat.wxtokenurl}")
    private String wxTokenUrl;
    @Value("${wechat.userurl}")
    private String userUrl;
    @Value("${wechat.redirecturi}")
    private String redirectUri;
    @Value("${wechat.loginurl}")
    private String loginUrl;

    //百度AI
    @Value("${baidu.appid}")
    private String appBID;
    @Value("${baidu.apikey}")
    private String apiBKey;
    @Value("${baidu.secretkey}")
    private String secretBKey;

    //用于存储微信AccessToken
    private static AccessToken at;

    //用于存储微信网页授权AccessToken
    private static WxAccessToken wxat;

    @Resource
    private UserSubInfoDao userSubInfoDaoImpl;

    private String openid;

    /**
     * 检验signature
     *
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    @Override
    public boolean check(String timestamp, String nonce, String signature) {
        //1）将token、timestamp、nonce三个参数进行字典序排序
        String[] strs = new String[]{token, timestamp, nonce};
        Arrays.sort(strs);
        //2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0] + strs[1] + strs[2];
        String mysig = WxUtil.sha1(str);//可以不自己加sha1()
        //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (mysig != null) {
            return mysig.equalsIgnoreCase(signature);
        }
        return false;
    }

    /**
     * XMl字符串转换成map
     *
     * @param is
     * @return
     */
    @Override
    public Map<String, String> parseRequest(InputStream is) {
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
    @Override
    public String getRespose(Map<String, String> requestMap) {
        BaseMessage msg = null;
        String msgType = requestMap.get("MsgType");
        switch (msgType) {
            case "text":
                String url = loginUrl.replace("REDIRECTURI", redirectUri);
                msg = MsgTypeUtil.dealTextMsg(requestMap, apiKey, apiUrl, userID, url);
                break;
            case "image":
                msg = MsgTypeUtil.dealImage(requestMap, appBID, apiBKey, secretBKey);
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
                msg = dealEvent(requestMap);
                break;
            default:
                break;
        }
        if (msg != null) {
            return beanToXml(msg);
        }
        return null;
    }

    /**
     * 处理用户操作的按钮事件
     *
     * @param requestMap
     * @return
     */
    private BaseMessage dealEvent(Map<String, String> requestMap) {
        String url = loginUrl.replace("REDIRECTURI", redirectUri);
        String event = requestMap.get("Event");
        switch (event) {
            case "CLICK":
                return MsgTypeUtil.dealClick(requestMap, url);
            case "VIEW":
                return MsgTypeUtil.dealView(requestMap);
            case "subscribe":
                openid = MsgTypeUtil.dealSubscribe(requestMap);
                System.out.println(openid + "正在关注");
                getUInfo(openid);
            default:
                break;
        }
        return null;
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
    private void getToken() {
        String url = tokenUrl;
        String tokenStr = WxUtil.get(url);
        JSONObject jsonObject = JSONObject.fromObject(tokenStr);
        String token = jsonObject.getString("access_token");
        String expireIn = jsonObject.getString("expires_in");
        //创建token对象,并存起来。
        at = new AccessToken(token, expireIn);
    }

    /**
     * 向外暴露的获取token的方法
     *
     * @return
     */
    @Override
    public String getAccessToken() {
        if (at == null || at.isExpired()) {
            getToken();
        }
        return at.getAccessToken();
    }

    /**
     * 上传临时素材
     *
     * @param path 上传的文件的路径
     * @param type 上传的文件类型
     * @return
     */
    public String upload(String path, String type) {
        File file = new File(path);
        OutputStream out = null;
        InputStream is = null;
        InputStream is2 = null;
        //地址
        String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
        try {
            URL urlObj = new URL(url);
            //强转为案例连接
            HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
            //设置连接的信息
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //设置请求头信息
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "utf8");
            //数据的边界
            String boundary = "-----" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //获取输出流
            out = conn.getOutputStream();
            //创建文件的输入流
            is = new FileInputStream(file);
            //第一部分：头部信息
            //准备头部信息
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition:form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            out.write(sb.toString().getBytes());
            System.out.println(sb.toString());
            //第二部分：文件内容
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                out.write(b, 0, len);
            }
            is.close();
            //第三部分：尾部信息
            String foot = "\r\n--" + boundary + "--\r\n";
            out.write(foot.getBytes());
            out.flush();
            //读取数据
            is2 = conn.getInputStream();
            StringBuilder resp = new StringBuilder();
            while ((len = is2.read(b)) != -1) {
                resp.append(new String(b, 0, len));
            }
            return resp.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (is != null) {
                    is.close();
                }
                if (is2 != null) {
                    is2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取带参数二维码的ticket
     *
     * @return
     */
    @Override
    public String getQrCodeTicket() {
        String at = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + at;
        //生成临时字符二维码
        String data = "{\"expire_seconds\": 600, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"hks\"}}}";
        String result = WxUtil.post(url, data);
        return JSONObject.fromObject(result).getString("ticket");
    }

    /**
     * 获取网页授权token
     */
    private void getWxToken(String code) {
        //换取accesstoken的地址
        String url = wxTokenUrl.replace("CODE", code);
        String result = WxUtil.get(url);
        JSONObject jsonObject = JSONObject.fromObject(result);
        String token = jsonObject.getString("access_token");
        String expireIn = jsonObject.getString("expires_in");
        openid = JSONObject.fromObject(result).getString("openid");

        //创建token对象,并存起来。
        wxat = new WxAccessToken(token, expireIn);
    }

    /**
     * 向外暴露的获取网页授权token的方法
     *
     * @return
     */
    public String getWxAccessToken(String code) {
        if (wxat == null || wxat.isExpired()) {
            getWxToken(code);
        }
        return wxat.getWxAccessToken();
    }

    /**
     * 获取已关注用户的基本信息
     *
     * @return
     */
    @Override
    public void getUInfo(String openid) {
        String url = uurl.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openid);
        //返回json
        String result = WxUtil.get(url);
        System.out.println(result);
        JSONObject jo = JSONObject.fromObject(result);
        jo.put("tagid_list", "\"" + jo.getString("tagid_list") + "\"");
        TbWechatUsersub user = (TbWechatUsersub) JSONObject.toBean(jo, TbWechatUsersub.class);
        user.setSubscribeDate(new Date());
        //user.setSubscribeTime(new Date(((long) jo.get("subscribe_time")) * 1000));
        int index = 0;
        index = userSubInfoDaoImpl.insTbWechatUserSub(user);
        System.out.println(index);
    }

    /**
     * 通过网页授权 获取用户信息
     *
     * @return
     */
    @Override
    public String getUserInfo(String code) {
        String at = getWxAccessToken(code);
        //拉取用户的基本信息
        String url = userUrl.replace("ACCESS_TOKEN", at).replace("OPENID", openid);
        return WxUtil.get(url);
    }
}
