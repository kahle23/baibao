package baibao.message.webhook.support;

import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Ignore
public class DingTalkRobotTest {

    @Test
    public void test1() {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        String webHook = "https://oapi.dingtalk.com/robot/send?access_token=xxxxxxxx";
        String secret = "xxxxxxxx";
        DingTalkRobot dingTalkRobot = new DingTalkRobot(webHook, secret);
        List<String> atList = new ArrayList<String>();
        atList.add("16688886666");
        dingTalkRobot.sendMarkdown("Test", "### Markdown Test\n\nTest", false, atList);
    }

}
