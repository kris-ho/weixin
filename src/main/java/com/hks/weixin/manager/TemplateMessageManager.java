package com.hks.weixin.manager;
import com.hks.weixin.service.WeChatService;
import com.hks.weixin.utils.WxUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TemplateMessageManager {
    @Autowired
    private WeChatService weChatServiceImpl;

    /**
     * 设置行业
     * by 罗召勇 Q群193557337
     */
    @Test
    public void set() {
        String at = weChatServiceImpl.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=" + at;
        String data = "{\n" +
                "          \"industry_id1\":\"1\",\n" +
                "          \"industry_id2\":\"4\"\n" +
                "       }";
        String result = WxUtil.post(url, data);
        System.out.println(result);
    }

    /**
     * 获取行业
     * by 罗召勇 Q群193557337
     */
    @Test
    public void get() {
        String at = weChatServiceImpl.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=" + at;
        String result = WxUtil.get(url);
        System.out.println(result);
    }

    /**
     * 发送模板消息
     * by 罗召勇 Q群193557337
     */
    @Test
    public void sendTemplateMessage() {
        String at = weChatServiceImpl.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + at;
        String data = "{\n" +
                "           \"touser\":\"oOI9Lw8GIP_7u71L1jr0_OAjJChE\",\n" +
                "           \"template_id\":\"Vr2evC4M2i6z-A1-LHnFe2VQQ_xKJiGJN-yc5nfPYNA\",         \n" +
                "           \"data\":{\n" +
                "                   \"first\": {\n" +
                "                       \"value\":\"您有新的反馈信息啦！\",\n" +
                "                       \"color\":\"#abcdef\"\n" +
                "                   },\n" +
                "                   \"company\":{\n" +
                "                       \"value\":\"hks公司\",\n" +
                "                       \"color\":\"#fff000\"\n" +
                "                   },\n" +
                "                   \"time\": {\n" +
                "                       \"value\":\"2000年11月11日\",\n" +
                "                       \"color\":\"#1f1f1f\"\n" +
                "                   },\n" +
                "                   \"result\": {\n" +
                "                       \"value\":\"面试通过\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"remark\":{\n" +
                "                       \"value\":\"请和本公司人事专员联系！\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        String result = WxUtil.post(url, data);
        System.out.println(result);
    }
}
