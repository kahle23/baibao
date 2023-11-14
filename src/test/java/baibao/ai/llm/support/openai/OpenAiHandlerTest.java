package baibao.ai.llm.support.openai;

import artoria.ai.AiUtils;
import artoria.data.Dict;
import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

@Ignore
public class OpenAiHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(OpenAiHandlerTest.class);
    private static final String handlerName = "openai";

    static {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        String apiKey = "apiKey";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 58591));
        AiUtils.registerHandler(handlerName, new OpenAiHandlerImpl(apiKey, proxy));
    }

    @Test
    public void testSpeechCreate() {
        // TTS models: tts-1 or tts-1-hd
        Dict args = Dict.of("model", "tts-1")
                // alloy, echo, fable, onyx, nova, shimmer
                .set("voice", "alloy")
                // mp3, opus, aac, flac
//                .set("response_format", "mp3")
                // 0.25 - 4.0    Defaults to 1.0
//                .set("speed", 1.0)
                .set("input", "This is test speech create! ")
                ;
        String method = "speechCreate";
        InputStream execute = AiUtils.execute(handlerName, args, method, InputStream.class);
        File file = FileUtil.writeFromStream(execute, "F:\\test\\testSpeechCreate.mp3");
        log.info("result: {}", file);
    }

    @Test
    public void testChat() {
        Dict args = Dict.of("model", "gpt-3.5-turbo-0613")
                .set("temperature", 1.9)
                .set("stream", true)
                .set("messages", Arrays.asList(
                        Dict.of("role", "system").set("content", "You are a helpful assistant."),
                        Dict.of("role", "user").set("content", "what is AI?"))
                )
        ;
        String method = "chat";
        Dict execute = AiUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void testCompletion() {
        Dict args = Dict.of("model", "gpt-3.5-turbo-instruct")
                .set("max_tokens", 7)
                .set("temperature", 0)
//                .set("stream", true)
                .set("prompt", "Say this is a test")
                ;
        String method = "completion";
        Dict execute = AiUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void testEmbedding() {
        Dict args = Dict.of("model", "text-embedding-ada-002")
                .set("encoding_format", "float")
                .set("input", "this is a test")
                ;
        String method = "embedding";
        Dict execute = AiUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void testImageCreate() {
        Dict args = Dict.of("model", "dall-e-3")
                .set("n", 1)
                // Defaults to standard
//                .set("quality", "hd")
                // url or b64_json
//                .set("response_format", "url")
                // 256x256, 512x512, or 1024x1024 for dall-e-2.
                // 1024x1024, 1792x1024, or 1024x1792 for dall-e-3
                .set("size", "1024x1024")
                // vivid or natural, Defaults to vivid
//                .set("style", "vivid")
                .set("prompt", "A cute baby sea otter. ")
                ;
        String method = "imageCreate";
        Dict execute = AiUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void testModels() {
        String method = "models";
        Dict execute = AiUtils.execute(handlerName, (Object) null, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void test3() {
        String strategy = "chat";
        String content = "目前有以下功能处理器：\n" +
                "名称：邮件发送处理器\n" +
                "关键词：邮件发送\n" +
                "\n" +
                "名称：知识库数据查询处理器\n" +
                "关键词：知识库、员工守则、公司规定、公司新规、工龄、工龄计算规则、请假\n" +
                "\n" +
                "名称：百度数据查询处理器\n" +
                "关键词：互联网数据查询、百度查询、百度数据\n" +
                "\n" +
                "名称：员工信息查询处理器\n" +
                "关键词：员工信息、员工、员工数据、入离职信息\n" +
                "\n" +
                "请根据用户问题，判断需要调用哪几个功能处理器，用英文逗号分隔，需要优先执行的在前，一般情况下数据查询类处理器需要优先执行。\n" +
                "不要写任何解释或其他文字和标点符号。\n" +
                "\n" +
                "用户问题：给张三发送离职信息邮件，邮件标题为张三的毕业祝贺，邮件内容为张三的基本信息的内容、张三的工龄（工龄需要根据公司的规定计算得出），并在邮件尾部加上祝您找到的更好的工作。";
        String execute = AiUtils.execute(handlerName, content, strategy, String.class);
        log.info(execute);
    }

}
