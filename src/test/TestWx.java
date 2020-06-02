import com.hks.weixin.pojo.*;
import com.hks.weixin.utils.WxService;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestWx {
    @Test
    public void testMsg() {
        Map<String, String> map = new HashMap<>();
        map.put("ToUserName", "to");
        map.put("FromUserName", "from");
        map.put("MsgType", "type");
        TextMessage tm = new TextMessage(map, "还好");
        XStream stream = new XStream();
        // 设置需要处理XStreamAlias("xml")注释的类
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMessage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VideoMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        String xml = stream.toXML(tm);
        System.out.println(xml);

    }
}
