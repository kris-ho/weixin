package com.hks.weixin.service;

import javax.servlet.ServletInputStream;
import java.io.InputStream;
import java.util.Map;

public interface WeChatService {
    /**
     * 检验signature
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
     boolean check(String timestamp, String nonce, String signature);

    /**
     * 用于处理所有的事件和消息回复
     * @param requestMap
     * @return
     */
    String getRespose(Map<String, String> requestMap);

    /**
     * 获取带参数二维码的ticket
     * @return
     */
    String getQrCodeTicket();

    /**
     * 通过网页授权 获取用户信息
     * @param code
     */
    void getUserInfo(String code);

    /**
     * Ml字符串转换成map
     * @param inputStream
     * @return
     */
    Map<String, String> parseRequest(InputStream inputStream);

    /**
     * 获取token
     * @return
     */
    String getAccessToken();
}
