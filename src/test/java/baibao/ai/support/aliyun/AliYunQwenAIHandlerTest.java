package baibao.ai.support.aliyun;

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

import java.util.Arrays;

import static kunlun.ai.support.model.Message.SYSTEM;
import static kunlun.ai.support.model.Message.USER;

@Ignore
public class AliYunQwenAIHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(AliYunQwenAIHandlerTest.class);
    private static final String handlerName = "qwen";
    private static final String embedModel = "text-embedding-v3";
    private static final String chatModel = "qwen-max-0919";

    static {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        AIUtils.registerHandler(handlerName, new AbstractAliYunQwenAIHandler() {
            @Override
            protected Config getConfig(Object input, String operation, Class<?> clazz) {
                Config config = new Config();
                config.setApiKey("sk-aaaa");
                config.setDebug(true);
                return config;
            }
        });
    }

    @Test
    public void testChat() {
        Dict args = Dict.of("model", chatModel)
//                .set("temperature", 1.9)
                .set("messages", Arrays.asList(
                        Dict.of("role", "system").set("content", "You are a helpful assistant."),
                        Dict.of("role", "user").set("content", "what is AI?"))
                )
        ;
        String method = "chat";
        Dict result = AIUtils.execute(handlerName, args, method, Dict.class);
        log.info("result: {}", JSON.toJSONString(result, Boolean.TRUE));
    }

    @Test
    public void testChat1() {
        ChatRequest request = ChatRequest.Builder.of(chatModel)
//                .setTemperature(1.9)
                .addMessage(SYSTEM, "You are a helpful assistant.")
                .addMessage(USER, "what is AI?")
                .build();
        String method = "chat";
        ChatResponse response = AIUtils.execute(handlerName, request, method, ChatResponse.class);
        log.info("result: {}", JSON.toJSONString(response, Boolean.TRUE));
    }

    @Test
    public void testChat2() {
        ChatRequest request = ChatRequest.Builder.of(chatModel)
//                .setTemperature(1.9)
                .setStream(true)
                .addMessage(SYSTEM, "You are a helpful assistant.")
                .addMessage(USER, "what is AI?")
                .setStreamConsumer(new Consumer<Object>() {
                    @Override
                    public void accept(Object param) {
                        String line = String.valueOf(param);
                        System.out.println(line);
                    }})
                .build();
        String method = "chat";
        AIUtils.execute(handlerName, request, method, Object.class);
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

}
