package baibao.ai.support.azure;

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
public class AzureOpenAIHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(AzureOpenAIHandlerTest.class);
    private static final String handlerName = "azure";
    private static final String embedModel1 = "text-embedding-3-large";
    private static final String embedModel = "text-embedding-ada-002";
    private static final String chatModel = "gpt-4";

    static {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        AIUtils.registerHandler(handlerName, new AbstractAzureOpenAIHandler() {
            @Override
            protected Config getConfig(Object input, String operation, Class<?> clazz) {
                Config config = new Config();
                config.setEndpoint("aaaa.openai.azure.com");
                config.setApiVersion("2023-05-15");
                config.setApiKey("aaaa");
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
