package baibao.message.webhook.support;

import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class WxWorkMessageRobotTest {
    private static Logger log = LoggerFactory.getLogger(WxWorkMessageRobotTest.class);
    private static WxWorkMessageRobot wxWorkMessageBot = new WxWorkMessageRobot("key123456");

    @Test
    public void test1() {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        for (int i = 0; i < 10; i++) {
            Object send = wxWorkMessageBot.send("Hello, World! ");
            log.info("Send result: {}", send);
        }
    }

}
