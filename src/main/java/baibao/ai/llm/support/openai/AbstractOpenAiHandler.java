package baibao.ai.llm.support.openai;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import kunlun.ai.support.AbstractClassicAiHandler;
import kunlun.common.constant.Symbols;
import kunlun.data.Dict;
import kunlun.data.StreamDataHandler;
import kunlun.data.bean.BeanUtils;
import kunlun.data.json.JsonUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.net.http.HttpMethod;
import kunlun.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Map;

import static kunlun.common.constant.Numbers.*;
import static kunlun.net.http.HttpMethod.GET;
import static kunlun.net.http.HttpMethod.POST;

/**
 * The ai engine based on the openai api.
 * @see <a href="https://platform.openai.com/docs/api-reference">API REFERENCE</a>
 * @author Kahle
 */
public abstract class AbstractOpenAiHandler extends AbstractClassicAiHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractOpenAiHandler.class);
    protected static final String STREAM_KEY = "stream";
    protected static final String PROMPT_KEY = "prompt";
    protected static final String ERROR_KEY  = "error";
    protected static final String MODEL_KEY  = "model";

    /**
     * Get the open ai engine configuration according to the arguments.
     * @param input The input arguments
     * @return The open ai engine configuration
     */
    protected abstract Config getConfig(Object input);

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

    protected void handleStream(InputStream inputStream, Charset charset, StreamDataHandler streamHandler) {
        try {
            // Create BufferedReader.
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, charset));
            //
            String line;
            while (!"data: [DONE]".equals(line = reader.readLine())) {
                streamHandler.handle(null, null, null, null, line + Symbols.LINE_FEED);
            }
            // last
            streamHandler.handle(null, null, null, null, line + Symbols.LINE_FEED);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    protected Object doHttp(HttpMethod method, Integer inputType, String url, Object input, Config config) {
        // Get stream value.
        Dict inputData = Dict.of(BeanUtils.beanToMap(input));
        StreamDataHandler streamHandler = (StreamDataHandler) inputData.get("streamDataHandler");
        inputData.remove("streamDataHandler");
        Boolean stream = inputData.getBoolean("stream");
        if (stream == null) { stream = false; }
        if (stream) {
            Assert.notNull(streamHandler, "When the parameter \"stream\" is true, " +
                    "the parameter \"streamDataHandler\" cannot be null. ");
        }
        // Get apiKey, proxy and debug.
        String apiKey = config.getApiKey();
        Proxy proxy = config.getProxy();
        Boolean debug = config.getDebug();
        // Create request.
        HttpRequest request = HttpRequest.of(url);
        // Set request method.
        if (POST.equals(method)) { request.method(Method.POST); }
        else if (GET.equals(method)) { request.method(Method.GET); }
        else {
            throw new UnsupportedOperationException("The http method is unsupported. ");
        }
        // Set request data.
        // Http input type: 0 unknown, 1 no content, 2 form-www, 3 form-data, 4 json
        if (input != null) {
            if (inputType == TWO || inputType == THREE) {
                request.form(BeanUtils.beanToMap(inputData));
            }
            else if (inputType == FOUR) {
                request.body(JsonUtils.toJsonString(inputData));
            }
        }
        // Set request header.
        request.header("Authorization", "Bearer " + apiKey);
        // Set request proxy.
        if (proxy != null) { request.setProxy(proxy); }
        // Record the log for input data.
        if (debug != null && debug) {
            log.info("The openai api request url \"{}\"(stream: {}) input is \"{}\"."
                    , url, stream, inputData);
        }
        // Processing response.
        if (stream) {
            // Is stream, but content-type no event-stream.
            // Most of the time it's because something went wrong.
            HttpResponse response = request.executeAsync();
            Charset charset = Charset.forName(response.charset());
            if (!response.header("Content-Type").contains("event-stream")) {
                String streamStrBody = response.body();
                Dict result = JsonUtils.parseObject(streamStrBody, Dict.class);
                checkResult(result);
                if (debug != null && debug) {
                    log.info("The openai api request url \"{}\"(stream: {}) response is \"{}\"."
                            , url, true, streamStrBody);
                }
                return result;
            }
            // Return InputStream.
            else {
                InputStream inputStream = response.bodyStream();
                handleStream(inputStream, charset, streamHandler);
                return null;
            }
        }
        else {
            // Non stream, but some time content-type no json.
            HttpResponse response = request.execute();
            if (response.header("Content-Type").contains("json")) {
                String body = response.body();
                if (debug != null && debug) {
                    log.info("The openai api request url \"{}\"(stream: {}) response is \"{}\"."
                            , url, false, body);
                }
                Dict respData = JsonUtils.parseObject(body, Dict.class);
                checkResult(respData);
                return respData;
            }
            // Return InputStream.
            else { return response.bodyStream(); }
        }
    }

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        if ("speechCreate".equals(strategy)) { return speechCreate(input, clazz); }
        else if ("transcriptionCreate".equals(strategy)) { return transcriptionCreate(input, clazz); }
        else if ("translationCreate".equals(strategy)) { return translationCreate(input, clazz); }
        else if ("chat".equals(strategy)) { return chat(input, clazz); }
        else if ("completion".equals(strategy)) { return completion(input, clazz); }
        else if ("embedding".equals(strategy)) { return embedding(input, clazz); }
        else if ("imageCreate".equals(strategy)) { return imageCreate(input, clazz); }
        else if ("imageEdit".equals(strategy)) { return imageEdit(input, clazz); }
        else if ("imageVariation".equals(strategy)) { return imageVariation(input, clazz); }
        else if ("modelList".equals(strategy)) { return modelList(input, clazz); }
        else {
            throw new UnsupportedOperationException(
                "The method is unsupported. \n\n" +
                "The openai ai handler. \n" +
                "(The arguments see api documents \"https://platform.openai.com/docs/api-reference\")\n" +
                "Supported method:\n" +
                " - speechCreate\n" +
                " - transcriptionCreate\n" +
                " - translationCreate\n" +
                " - chat\n" +
                " - completion\n" +
                " - embedding\n" +
                " - imageCreate\n" +
                " - imageEdit\n" +
                " - imageVariation\n" +
                " - modelList\n"
            );
        }
    }

    /**
     * Generates audio from the input text.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/audio/createSpeech">
     *     Create speech</a>
     */
    public Object speechCreate(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/audio/speech";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Transcribes audio into the input language.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/audio/createTranscription">
     *     Create transcription</a>
     */
    public Object transcriptionCreate(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/audio/transcriptions";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Translates audio into English.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/audio/createTranslation">
     *     Create translation</a>
     */
    public Object translationCreate(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/audio/translations";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Given a list of messages comprising a conversation, the model will return a response.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/chat/create">
     *     Create chat completion</a>
     */
    public Object chat(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/chat/completions";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Given a prompt, the model will return one or more predicted completions,
     *      and can also return the probabilities of alternative tokens at each position.
     *      We recommend most users use our Chat Completions API.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/completions/create">
     *     Create completion</a>
     */
    public Object completion(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/completions";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Get a vector representation of a given input that can be easily consumed by
     *      machine learning models and algorithms.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/embeddings/create">
     *     Create embeddings</a>
     */
    public Object embedding(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/embeddings";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Creates an image given a prompt.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/images/create">
     *     Create image</a>
     */
    public Object imageCreate(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/images/generations";
        return doHttp(POST, FOUR, url, input, config);
    }

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/images/createEdit">
     *     Create image edit</a>
     */
    public Object imageEdit(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/images/edits";
        return doHttp(POST, THREE, url, input, config);
    }

    /**
     * Creates a variation of a given image.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/images/createVariation">
     *     Create image variation</a>
     */
    public Object imageVariation(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/images/variations";
        return doHttp(POST, THREE, url, input, config);
    }

    /**
     * Lists the currently available models, and provides basic information about
     *      each one such as the owner and availability.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/models/list">
     *     List models</a>
     */
    public Object modelList(Object input, Class<?> clazz) {
        Config config = getConfig(input);
        String url = "https://api.openai.com/v1/models";
        return doHttp(GET, ONE, url, input, config);
    }

    /**
     * The openai ai engine configuration.
     * @author Kahle
     */
    public static class Config implements Serializable {
        private String apiKey;
        private Proxy  proxy;
        private Boolean debug;

        public Config(String apiKey, Proxy proxy) {
            Assert.notBlank(apiKey, "Parameter \"apiKey\" must not blank. ");
            this.apiKey = apiKey;
            this.proxy = proxy;
        }

        public Config(String apiKey) {

            this(apiKey, null);
        }

        public Config() {

        }

        public String getApiKey() {

            return apiKey;
        }

        public void setApiKey(String apiKey) {

            this.apiKey = apiKey;
        }

        public Proxy getProxy() {

            return proxy;
        }

        public void setProxy(Proxy proxy) {

            this.proxy = proxy;
        }

        public Boolean getDebug() {

            return debug;
        }

        public void setDebug(Boolean debug) {

            this.debug = debug;
        }
    }

}
