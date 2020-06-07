package com.hks.weixin.controller;

import com.hks.weixin.utils.WxService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class WxController {
    @Value("${token}")
    private String token;

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
        if (WxService.check(token, timestamp, nonce, signature)) {
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
    @PostMapping("/wx")
    public void weixinPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；

        //处理消息和事件推送
        Map<String, String> requestMap = WxService.parseRequest(request.getInputStream());
        System.out.println(requestMap);

        //准备回复的数据包
        String respXml = WxService.getRespose(requestMap);
        System.out.println(respXml);
        PrintWriter out = response.getWriter();
        out.print(respXml);
        out.flush();
        out.close();
    }
}