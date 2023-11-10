package baibao.ai.llm.support.ycm;

import artoria.ai.AiUtils;
import artoria.data.Dict;
import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class YcmAiEngineTest {
    private static final Logger log = LoggerFactory.getLogger(YcmAiEngineTest.class);
    private static final String engineName = "ycm";

    static {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        String accessKey = "accessKey";
        String secretKey = "secretKey";
        AiUtils.registerEngine(engineName, new YcmAiEngineImpl(accessKey, secretKey));
    }

    @Test
    public void testChat() {
        Dict args = Dict.of("message", "what is AI?");
        Dict execute = AiUtils.execute(engineName, args, "chat", Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

}
