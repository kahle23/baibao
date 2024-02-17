package baibao.ai.llm.support.openai;

import kunlun.ai.AiUtils;
import kunlun.data.Dict;
import kunlun.data.StreamDataHandler;
import kunlun.data.json.JsonUtils;
import kunlun.data.json.support.FastJsonHandler;
import baibao.ai.llm.dto.chat.ChatMessage;
import baibao.ai.llm.dto.chat.ChatReq;
import baibao.ai.llm.dto.chat.ChatResp;
import baibao.ai.llm.dto.chat.Tool;
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
import java.util.ArrayList;
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
//                .set("stream", true)
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
    public void testChat1() {
        ChatReq req = new ChatReq(new ArrayList<ChatMessage>(), "gpt-3.5-turbo-0613");
//        req.setTemperature(1.9);
        req.getMessages().add(new ChatMessage("system", "You are a helpful assistant."));
        req.getMessages().add(new ChatMessage("user", "what is AI?"));
        String method = "chat";
        ChatResp resp = AiUtils.execute(handlerName, req, method, ChatResp.class);
        log.info("result: {}", JSON.toJSONString(resp, Boolean.TRUE));
    }

    @Test
    public void testChat2() {
        ChatReq req = new ChatReq(new ArrayList<ChatMessage>(), "gpt-3.5-turbo-0613");
//        req.setTemperature(1.9);
        req.setStream(true);
        req.getMessages().add(new ChatMessage("system", "You are a helpful assistant."));
        req.getMessages().add(new ChatMessage("user", "what is AI?"));
        req.setStreamDataHandler(new StreamDataHandler() {
            @Override
            public void handle(Object... arguments) {
                String line = String.valueOf(arguments[4]);
                System.out.println(line);
//                if (StrUtil.isBlank(line)) { return; }
//                line = line.trim();
//                String substring = line.substring("data: ".length());
//                Dict dict = JSON.parseObject(substring, Dict.class);
//                Array choices = Array.of((List) dict.get("choices"));
//                Object delta = Dict.of(BeanUtils.beanToMap(choices.get(0))).get("delta");
//                String content = Dict.of(BeanUtils.beanToMap(delta)).getString("content");
//                System.out.print(content);
            }
        });
        String method = "chat";
        AiUtils.execute(handlerName, req, method, Object.class);
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

}
