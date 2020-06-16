package com.hks.weixin.controller;

import com.hks.weixin.mapper.TbWechatUserMapper;
import com.hks.weixin.service.UserInfoDao;
import com.hks.weixin.pojo.*;
import com.hks.weixin.service.WeChatService;
import com.hks.weixin.utils.WxUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Controller
public class WeChatController {
    //微信服务
    @Resource
    private WeChatService weChatServiceImpl;

    @Resource
    private UserInfoDao userInfoDaoImpl;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 微信接入
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/wx")
    public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");//随机字符串
        if (weChatServiceImpl.check(timestamp, nonce, signature)) {
            PrintWriter out = response.getWriter();
            out.print(echostr);
            out.flush();
            out.close();
        } else {
            System.out.println("接入失败");
        }
    }

    /**
     * 接收消息和推送消息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/wx", produces = "application/xml;charset=utf-8")
    public void weixinPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；

        //处理消息和事件推送
        Map<String, String> requestMap = weChatServiceImpl.parseRequest(request.getInputStream());
        //System.out.println(requestMap);

        //准备回复的数据包
        String respXml = weChatServiceImpl.getRespose(requestMap);
        System.out.println(respXml);
        PrintWriter out = response.getWriter();
        out.print(respXml);
        out.flush();
        out.close();
    }

    /**
     * 获取ticket
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/GetTicket")
    public void getTicket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String ticket = weChatServiceImpl.getQrCodeTicket();
        out.print(ticket);
        out.flush();
        out.close();
    }

    /**
     * 通过网页授权 获取用户信息
     *
     * @param request
     * @param response
     */
    @GetMapping("/GetUserInfo")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        //获取code
        String code = request.getParameter("code");
        String result = weChatServiceImpl.getUserInfo(code);
        JSONObject jo = JSONObject.fromObject(result);
        jo.put("privilege", "\"" + jo.getString("privilege") + "\"");
        System.out.println(jo.getString("privilege"));
        TbWechatUser user = (TbWechatUser) JSONObject.toBean(jo, TbWechatUser.class);
        user.setSubscribe((byte) 1);
        user.setSubscribeTime(new Date());
        userInfoDaoImpl.insTbWechatUser(user);
    }

    /**
     * 创建菜单
     */
    @GetMapping("/CreateMenu")
    public void createMenu() {
        //准备url
        String urlGet = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN";
        urlGet = urlGet.replace("ACCESS_TOKEN", weChatServiceImpl.getAccessToken());
        //发送请求
        String resultGet = WxUtil.get(urlGet);
        if ((resultGet != null) && !"".equals(resultGet)) {
            //准备url
            String urlde = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
            urlde = urlde.replace("ACCESS_TOKEN", weChatServiceImpl.getAccessToken());
            //发送请求
            String resultde = WxUtil.get(urlde);
            System.out.println(resultde);
        }

        //菜单对象
        Button btn = new Button();

        //创建第一个一级菜单
        SubButton sb1 = new SubButton("撩一撩");
        //为第一个一级菜单增加子菜单
        sb1.getSub_button().add(new PhotoOrAlbumButton("传图识文字", "31"));
        sb1.getSub_button().add(new ClickButton("登录", "32"));
        //加入第一个一级菜单
        btn.getButton().add(sb1);

        //创建第二个一级菜单
        SubButton sb2 = new SubButton("目录");
        //为第二个一级菜单增加子菜单
        sb2.getSub_button().add(new ViewButton("历史消息", "http://news.163.com"));
        //加入第二个一级菜单
        btn.getButton().add(sb2);

        //创建第三个一级菜单
        SubButton sb = new SubButton("关于");
        //为第三个一级菜单增加子菜单
        sb.getSub_button().add(new ViewButton("关于作者", "http://www.karshen.com/"));
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
