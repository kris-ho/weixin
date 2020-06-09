package com.hks.weixin.controller;

import com.hks.weixin.service.WeChatService;
import com.hks.weixin.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class WeChatController {
    @Autowired
    private WeChatService weChatServiceImpl;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 微信接入
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
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/wx")
    public void weixinPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；

        //处理消息和事件推送
        Map<String, String> requestMap = weChatServiceImpl.parseRequest(request.getInputStream());
        System.out.println(requestMap);

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
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/GetTicket")
    public void getTicket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String ticket = weChatServiceImpl.getQrCodeTicket();
        out.print(ticket);
        out.flush();
        out.close();
    }

    /**
     * 通过网页授权 获取用户信息
     * @param request
     * @param response
     */
    @GetMapping("/GetUserInfo")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        //获取code
        String code = request.getParameter("code");
        weChatServiceImpl.getUserInfo(code);
    }
}
