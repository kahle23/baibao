package baibao.ai.llm.support.openai;

import artoria.ai.AbstractClassicAiEngine;
import artoria.ai.llm.LLM;
import artoria.ai.llm.support.AiMessage;
import artoria.data.Dict;
import artoria.data.bean.BeanUtils;
import artoria.data.json.JsonUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.singletonList;

/**
 * The ai engine based on the openai api.
 * @see <a href="https://platform.openai.com/docs/api-reference">API REFERENCE</>
 * @author Kahle
 */
public class OpenAiEngine extends AbstractClassicAiEngine implements LLM {
    private static final Logger log = LoggerFactory.getLogger(OpenAiEngine.class);
    protected static final String STREAM_KEY = "stream";
    protected static final String PROMPT_KEY = "prompt";
    protected static final String ERROR_KEY  = "error";
    protected static final String MODEL_KEY  = "model";
    private String apiKey;
    private Proxy proxy;

    public OpenAiEngine(String apiKey) {
        Assert.notBlank(apiKey, "Parameter \"apiKey\" must not blank. ");
        this.apiKey = apiKey;
    }

    protected OpenAiEngine() {

    }

    public Proxy getProxy() {

        return proxy;
    }

    public void setProxy(Proxy proxy) {

        this.proxy = proxy;
    }

    protected String getApiKey() {
        Assert.notBlank(apiKey, "Parameter \"apiKey\" must not blank. ");
        return apiKey;
    }

    protected Dict convertToDict(Object input) {
        if (input instanceof Dict) {
            return (Dict) input;
        }
        else if (input instanceof Map) {
            return Dict.of((Map<?, ?>) input);
        }
        else {
            return Dict.of(BeanUtils.beanToMap(input));
        }
    }

    protected String get(String url, String body) {
        HttpRequest request = HttpRequest.get(url)
                .header("Authorization", "Bearer " + getApiKey());
        if (StrUtil.isNotBlank(body)) {
            request.body(body);
        }
        if (getProxy() != null) {
            request.setProxy(getProxy());
        }
        return request.execute().body();
    }

    protected Object post(String url, String body, Boolean stream) {
        if (stream == null) { stream = false; }
        HttpRequest request = HttpRequest.post(url)
                .header("Authorization", "Bearer " + getApiKey())
                .body(body);
        if (getProxy() != null) {
            request.setProxy(getProxy());
        }
        if (stream) {
            HttpResponse response = request.executeAsync();
            if (!response.header("Content-Type").contains("event-stream")) {
                String body1 = response.body();
                // response parse
                Dict result = JsonUtils.parseObject(body1, Dict.class);
                // result check
                checkResult(result);
                return new ByteArrayInputStream(body1.getBytes(Charset.forName(response.charset())));
            }
            else {
                return response.bodyStream();
            }
        }
        else {
            return request.execute().body();
        }
    }

    protected void checkResult(Map<?, ?> map) {
        Dict result = map instanceof Dict ? (Dict) map : Dict.of(map);
        Object errorObj = result.get(ERROR_KEY);
        if (errorObj == null) { return; }
        Dict error = Dict.of(errorObj instanceof Map
                ? (Map<?, ?>) errorObj : BeanUtils.beanToMap(errorObj));
        if (MapUtil.isEmpty(error)) { return; }
        String message = error.getString("message");
        String code = error.getString("code");
        Assert.state(StrUtil.isBlank(code)&&StrUtil.isBlank(message), message);
    }

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        if ("models".equals(strategy)) {
            return models(input, strategy, clazz);
        }
        else if ("chat".equals(strategy)) {
            return chat(input, strategy, clazz);
        }
        else if ("completion".equals(strategy)) {
            return completion(input, strategy, clazz);
        }
        else if ("embedding".equals(strategy)) {
            return embedding(input, strategy, clazz);
        }
        else { return chat(input, strategy, clazz); }
    }

    protected Object models(Object input, String strategy, Class<?> clazz) {
        // Parameter clazz validation
        Assert.isSupport(clazz, FALSE, Dict.class);
        // open ai api invoke.
        log.debug("The openai list models api request \"\". ");
        String response = get("https://api.openai.com/v1/models", null);
        log.debug("The openai list models api response \"{}\". ", response);
        // response parse
        Dict result = JsonUtils.parseObject(response, Dict.class);
        // result check
        checkResult(result);
        // convert result
        return result;
    }

    protected Object chat(Object input, String strategy, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        // data conversion.
        Dict data;
        if (input instanceof String) {
            data = Dict.of("messages", singletonList(Dict.of("role", "user").set("content", input)));
        }
        else if (input instanceof AiMessage) {
            data = Dict.of("messages", singletonList(input));
        }
        else { data = convertToDict(input); }
        // parameters validation and default value handle.
        boolean stream = data.getBoolean(STREAM_KEY, FALSE);
        String model = data.getString(MODEL_KEY);
//        Assert.notBlank(prompt, "Parameter \"prompt\" must not blank. ");
        if (StringUtils.isBlank(model)) { data.set(MODEL_KEY, "gpt-3.5-turbo-0613"); }
        // Parameter clazz validation
        if (stream) { Assert.isSupport(clazz, FALSE, InputStream.class); }
        else { Assert.isSupport(clazz, FALSE, String.class, Dict.class); }
        // open ai api invoke.
//        data.set("temperature", 1.9);
        String json = JsonUtils.toJsonString(data);
        log.info("The openai chat api request \"{}\". ", json);
        Object response = post("https://api.openai.com/v1/chat/completions", json, stream);
        // response is InputStream.
        if (stream) { return response; }
        // response is string
        String body = String.valueOf(response);
        log.info("The openai chat api response \"{}\". ", body);
        // response parse
        Dict result = JsonUtils.parseObject(body, Dict.class);
        // result check
        checkResult(result);
        // convert result
        if (CharSequence.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("rawtypes")
            List choices = (List) result.get("choices");
            @SuppressWarnings("rawtypes")
            Map first = (Map) CollectionUtils.getFirst(choices);
            @SuppressWarnings("rawtypes")
            Map message = first != null ? (Map) first.get("message") : null;
            String text = message != null ? (String) message.get("content") : null;
            Assert.notBlank(text, "The openai chat api first choice message content is blank. ");
            return text;
        }
        else { return result; }
    }

    protected Object completion(Object input, String strategy, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        // data conversion.
        Dict data;
        if (input instanceof String) {
            data = Dict.of(PROMPT_KEY, input);
        }
        else { data = convertToDict(input); }
        // parameters validation and default value handle.
        boolean stream = data.getBoolean(STREAM_KEY, FALSE);
        String prompt = data.getString(PROMPT_KEY);
        String model = data.getString(MODEL_KEY);
        Assert.notBlank(prompt, "Parameter \"prompt\" must not blank. ");
        if (StringUtils.isBlank(model)) { data.set(MODEL_KEY, "text-davinci-003"); }
        // Parameter clazz validation
        if (stream) { Assert.isSupport(clazz, FALSE, InputStream.class); }
        else { Assert.isSupport(clazz, FALSE, String.class, Dict.class); }
        // open ai api invoke.
        String json = JsonUtils.toJsonString(data);
        log.debug("The openai completions api request \"{}\". ", json);
        Object response = post("https://api.openai.com/v1/completions", json, stream);
        // response is InputStream.
        if (stream) { return response; }
        // response is string
        String body = String.valueOf(response);
        log.debug("The openai completions api response \"{}\". ", body);
        // response parse
        Dict result = JsonUtils.parseObject(body, Dict.class);
        // result check
        checkResult(result);
        // convert result
        if (CharSequence.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("rawtypes")
            List choices = (List) result.get("choices");
            @SuppressWarnings("rawtypes")
            Map first = (Map) CollectionUtils.getFirst(choices);
            String text = first != null ? (String) first.get("text") : null;
            Assert.notBlank(text, "The openai completions api first choice text is blank. ");
            return text;
        }
        else { return result; }
    }

    protected Object embedding(Object input, String strategy, Class<?> clazz) {

        throw new UnsupportedOperationException();
    }

}
