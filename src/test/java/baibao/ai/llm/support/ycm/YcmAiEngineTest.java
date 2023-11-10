package baibao.ai.llm.support.ycm;

import artoria.ai.AiUtils;
import artoria.data.Dict;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class YcmAiEngineTest {
    private static final Logger log = LoggerFactory.getLogger(YcmAiEngineTest.class);
    private static final String engineName = "ycm";

    static {
        YcmAiEngine aiEngine = new YcmAiEngine(
                "rlh8p35t87vqyw95sl4wnew3trh8ay92", "0zkx8r0dcgivf7tdwfv2tmm46b353zop");
        AiUtils.registerEngine(engineName, aiEngine);
    }

    @Test
    public void test1() {
        String execute = AiUtils.execute(engineName, "what is AI?", String.class);
        log.info(execute);
    }

    @Test
    public void test2() {
        Dict message = Dict.of("modelId", "1651468516836098050").set("message", "hello, world!");
        String execute = AiUtils.execute(engineName, message, String.class);
        log.info(execute);
    }

}
