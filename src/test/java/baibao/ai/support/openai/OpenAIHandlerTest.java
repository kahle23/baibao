package baibao.ai.support.openai;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import kunlun.ai.AIUtils;
import kunlun.ai.support.model.ChatRequest;
import kunlun.ai.support.model.ChatResponse;
import kunlun.core.function.Consumer;
import kunlun.data.Dict;
import kunlun.data.json.JsonUtils;
import kunlun.data.json.support.FastJsonHandler;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Arrays;

@Ignore
public class OpenAIHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(OpenAIHandlerTest.class);
    private static final String handlerName = "openai";
    private static final String embedModel = "text-embedding-ada-002";
    private static final String chatModel = "gpt-3.5-turbo-0613";

    static {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        AIUtils.registerHandler(handlerName, new AbstractOpenAIHandler() {
            @Override
            protected Config getConfig(Object input, String operation, Class<?> clazz) {
                Config config = new Config();
                config.setApiKey("apiKey");
                config.setProxyType(Proxy.Type.HTTP.name());
                config.setProxyHostname("127.0.0.1");
                config.setProxyPort(58591);
                config.setDebug(Boolean.FALSE);
                return config;
            }
        });
    }

    @Test
    public void testChat() {
        Dict args = Dict.of("model", chatModel)
                .set("temperature", 1.9)
//                .set("stream", true)
                .set("messages", Arrays.asList(
                        Dict.of("role", "system").set("content", "You are a helpful assistant."),
                        Dict.of("role", "user").set("content", "what is AI?"))
                );
        String method = "chat";
        Dict execute = AIUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void testChat1() {
        ChatRequest req = ChatRequest.Builder.of(chatModel)
//                .setTemperature(1.9)
                .addMessage("system", "You are a helpful assistant.")
                .addMessage("user", "what is AI?")
                .build();
        String method = "chat";
        ChatResponse resp = AIUtils.execute(handlerName, req, method, ChatResponse.class);
        log.info("result: {}", JSON.toJSONString(resp, Boolean.TRUE));
    }

    @Test
    public void testChat2() {
        ChatRequest req = ChatRequest.Builder.of(chatModel)
//                .setTemperature(1.9)
                .addMessage("system", "You are a helpful assistant.")
                .addMessage("user", "what is AI?")
                .setStream(true)
                .setStreamConsumer(new Consumer<Object>() {
                    @Override
                    public void accept(Object param) {
                        String line = String.valueOf(param);
                        System.out.println(line);
//                        if (StrUtil.isBlank(line)) { return; }
//                        line = line.trim();
//                        String substring = line.substring("data: ".length());
//                        Dict dict = JSON.parseObject(substring, Dict.class);
//                        Array choices = Array.of((List) dict.get("choices"));
//                        Object delta = Dict.of(BeanUtils.beanToMap(choices.get(0))).get("delta");
//                        String content = Dict.of(BeanUtils.beanToMap(delta)).getString("content");
//                        System.out.print(content);
                    }
                })
                .build();
        String method = "chat";
        AIUtils.execute(handlerName, req, method, Object.class);
    }

    @Test
    public void testEmbeddings() {
        Dict args = Dict.of("model", embedModel)
                .set("encoding_format", "float")
                .set("input", "this is a test")
                ;
        String method = "embeddings";
        Dict execute = AIUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
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
        InputStream execute = AIUtils.execute(handlerName, args, method, InputStream.class);
        File file = FileUtil.writeFromStream(execute, "F:\\test\\testSpeechCreate.mp3");
        log.info("result: {}", file);
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
        Dict execute = AIUtils.execute(handlerName, args, method, Dict.class);
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
        Dict execute = AIUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void testModels() {
        String method = "models";
        Dict execute = AIUtils.execute(handlerName, (Object) null, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(execute, Boolean.TRUE));
    }

}
