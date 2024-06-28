/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import kunlun.ai.support.AbstractStrategyAIHandler;
import kunlun.ai.support.model.*;
import kunlun.common.constant.Symbols;
import kunlun.convert.ConversionUtils;
import kunlun.core.function.Consumer;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtils;
import kunlun.data.json.JsonUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.net.http.HttpMethod;
import kunlun.util.Assert;
import kunlun.util.MapUtils;
import kunlun.util.ObjectUtils;
import kunlun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Numbers.*;
import static kunlun.net.http.HttpMethod.GET;
import static kunlun.net.http.HttpMethod.POST;

public abstract class AbstractHttpApiAIHandler extends AbstractStrategyAIHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractHttpApiAIHandler.class);
    protected static final String STREAM_KEY = "stream";
    protected static final String PROMPT_KEY = "prompt";
    protected static final String ERROR_KEY  = "error";
    protected static final String MODEL_KEY  = "model";
    protected static final String CONFIG_KEY = "config";
    protected static final String STREAM_CONSUMER_KEY = "streamConsumer";
    protected static final String AUTHORIZATION_KEY = "Authorization";
    protected static final String BEARER_KEY = "Bearer ";


    protected Object processOutput(InnerTool tool, Dict respDict, Class<?> clazz) {
        if (MapUtils.isEmpty(respDict) || clazz == null) { return respDict; }
        if (ChatResponse.class.isAssignableFrom(clazz)) {
            return tool.toChatResponse(respDict);
        }
        else if (EmbedResponse.class.isAssignableFrom(clazz)) {
            return tool.toEmbedResponse(respDict);
        }
        else { return respDict; }
    }

    protected Dict processInput(InnerTool tool, Object input) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        if (input instanceof ChatRequest) {
            return tool.fromChatRequest((ChatRequest) input);
        }
        else if (input instanceof EmbedRequest) {
            return tool.fromEmbedRequest((EmbedRequest) input);
        }
        else { return tool.toDict(input); }
    }

    /**
     * Handling stream response.
     * @param inputStream The response input stream
     * @param charset The response charset
     * @param consumer The stream response process logic
     */
    protected void doStream(InputStream inputStream, Charset charset, Consumer<Object> consumer) {
        try {
            // Create BufferedReader.
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, charset));
            // Read line and consume.
            String line;
            while (!"data: [DONE]".equals(line = reader.readLine())) {
                consumer.accept(line + Symbols.LINE_FEED);
            }
            // Last line.
            consumer.accept(line + Symbols.LINE_FEED);
        }
        catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * Executing based on http.
     * @param httpData The http data
     * @return The http result
     */
    protected Dict doHttp(HttpData httpData) {
        // Get parameters.
        AbstractConfig config = httpData.getConfig();
        InnerTool tool = httpData.getTool();
        Dict data = httpData.getData();
        // Get stream value and debug.
        @SuppressWarnings("unchecked")
        Consumer<Object> streamConsumer = (Consumer<Object>) data.remove(STREAM_CONSUMER_KEY);
        boolean stream = data.getBoolean(STREAM_KEY, Boolean.FALSE);
        if (stream) { Assert.notNull(streamConsumer
                , "When stream is true, the stream consumer cannot be null. "); }
        Boolean debug = config.getDebug();
        // If the config is included, delete it
        data.remove(CONFIG_KEY);
        // Create request.
        HttpRequest request = HttpRequest.of(httpData.getUrl());
        // Set request method.
        if (POST.equals(httpData.getMethod())) { request.method(Method.POST); }
        else if (GET.equals(httpData.getMethod())) { request.method(Method.GET); }
        else { throw new UnsupportedOperationException("The http method is unsupported. "); }
        // Set request data.
        // Http input type: 0 unknown, 1 no content, 2 form-www, 3 form-data, 4 json
        Integer httpType = httpData.getHttpType();
        if (httpType == TWO || httpType == THREE) {
            request.form(data);
        }
        else if (httpType == FOUR) {
            request.body(JsonUtils.toJsonString(data));
        }
        else if (httpType == ONE) {
            // no content
        }
        else { throw new UnsupportedOperationException("The http input type is unsupported. "); }
        // Set request header.
        if (MapUtil.isNotEmpty(httpData.getHeaders())) {
            for (Map.Entry<String, Object> entry : httpData.getHeaders().entrySet()) {
                Object val = entry.getValue();
                request.header(entry.getKey(), val != null ? String.valueOf(val) : null);
            }
        }
        // Set request proxy.
        if (StringUtils.isNotBlank(config.getProxyType()) &&
                StringUtils.isNotBlank(config.getProxyHostname())) {
            request.setProxy(tool.buildProxy(config));
        }
        // Record the log for input data.
        tool.logRequest(debug, httpData.getUrl(), data);
        // Processing response.
        if (!stream) {
            // Non stream.
            HttpResponse response = request.execute();
            String body = response.body();
            tool.logResponse(debug, httpData.getUrl(), body);
            Dict respData = JsonUtils.parseObject(body, Dict.class);
            tool.checkResult(respData);
            return respData;
        }
        // Is stream, but content-type no event-stream.
        // Most of the time it's because something went wrong.
        HttpResponse response = request.executeAsync();
        Charset charset = Charset.forName(response.charset());
        if (!response.header("Content-Type").contains("event-stream")) {
            String body = response.body();
            tool.logResponse(debug, httpData.getUrl(), body);
            Dict respData = JsonUtils.parseObject(body, Dict.class);
            tool.checkResult(respData);
            return respData;
        }
        // Return InputStream.
        else {
            InputStream inputStream = response.bodyStream();
            doStream(inputStream, charset, streamConsumer);
            return null;
        }
    }

    /**
     * The internal http data.
     * @author Kahle
     */
    protected static class HttpData {
        private InnerTool tool;
        private AbstractConfig config;
        private HttpMethod method;
        private Integer httpType;
        private String url;
        private Dict headers;
        private Dict data;

        public static HttpData of(InnerTool tool) {

            return of().setTool(tool);
        }

        public static HttpData of() {

            return new HttpData();
        }

        public InnerTool getTool() {

            return tool;
        }

        public HttpData setTool(InnerTool tool) {
            this.tool = tool;
            return this;
        }

        public AbstractConfig getConfig() {

            return config;
        }

        public HttpData setConfig(AbstractConfig config) {
            this.config = config;
            return this;
        }

        public HttpMethod getMethod() {

            return method;
        }

        public HttpData setMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Integer getHttpType() {

            return httpType;
        }

        public HttpData setHttpType(Integer httpType) {
            this.httpType = httpType;
            return this;
        }

        public String getUrl() {

            return url;
        }

        public HttpData setUrl(String url) {
            this.url = url;
            return this;
        }

        public Dict getHeaders() {

            return headers;
        }

        public HttpData setHeaders(Dict headers) {
            this.headers = headers;
            return this;
        }

        public Dict getData() {

            return data;
        }

        public HttpData setData(Dict data) {
            this.data = data;
            return this;
        }
    }

    /**
     * The internal tool.
     * @author Kahle
     */
    protected static class InnerTool {

        public Dict toDict(Object obj) {
            if (obj instanceof Dict) { return (Dict) obj; }
            return Dict.of(BeanUtils.beanToMap(obj));
        }

        public void logRequest(Boolean debug, String url, Object data) {
            if (debug != null && debug) {
                String json = JsonUtils.toJsonString(data);
                log.info("The http request url \"{}\" input is \"{}\".", url, json);
            }
        }

        public void logResponse(Boolean debug, String url, Object body) {
            if (debug != null && debug) {
                log.info("The http request url \"{}\" response is \"{}\".", url, body);
            }
        }

        public Dict fromChatRequest(ChatRequest request) {
            return Dict.of("model", request.getModel())
                    .set("temperature", request.getTemperature())
                    .set("top_p", request.getTopP())
                    .set("max_tokens",  request.getMaxTokens())
                    .set("n",  request.getN())
                    .set("stream",    request.getStream())
                    .set("streamConsumer", request.getStreamConsumer())
                    .set("messages", request.getMessages())
                    .set("config", request.getConfig())
            ;
        }

        public ChatResponse toChatResponse(Dict respDict) {
            ChatResponse.Builder builder = ChatResponse.Builder.of();
            builder.setId(respDict.getString("id"));
            // Convert choices.
            @SuppressWarnings("rawtypes")
            List choices = (List) respDict.get("choices");
            for (Object choice : choices) {
                Dict    choiceDict = Dict.of(BeanUtils.beanToMap(choice));
                String  reason = choiceDict.getString("finish_reason");
                Integer index = choiceDict.getInteger("index");
                Map<String, Object> messageMap = ObjectUtils.cast(choiceDict.get("message"));
                Message message = BeanUtils.mapToBean(messageMap, Message.class);
                builder.addChoice(index, message, reason);
            }
            // Convert usage.
            Dict usageDict = Dict.of(BeanUtils.beanToMap(respDict.get("usage")));
            builder.setUsage(Usage.Builder.of()
                    .setPromptTokens(usageDict.getInteger("prompt_tokens"))
                    .setCompletionTokens(usageDict.getInteger("completion_tokens"))
                    .setTotalTokens(usageDict.getInteger("total_tokens"))
                    .build()
            );
            // Build.
            return builder.build();
        }

        public Dict fromEmbedRequest(EmbedRequest request) {
            return Dict.of("input", request.getInput())
                    .set("model", request.getModel())
                    .set("encoding_format", request.getEncodingFormat())
                    .set("dimensions", request.getDimensions())
                    .set("config", request.getConfig())
            ;
        }

        public EmbedResponse toEmbedResponse(Dict respDict) {
            EmbedResponse.Builder builder = EmbedResponse.Builder.of();
            builder.setModel(respDict.getString("model"));
            builder.setData(new ArrayList<EmbedData>());
            // Convert data.
            @SuppressWarnings("rawtypes")
            List list = (List) respDict.get("data");
            for (Object datum : list) {
                Dict datumDict = Dict.of(BeanUtil.beanToMap(datum));
                // Convert embedding.
                List<Float> embeddingFl = new ArrayList<Float>();
                @SuppressWarnings("rawtypes")
                List embedding = (List) datumDict.get("embedding");
                for (Object obj : embedding) {
                    if (obj == null) { continue; }
                    embeddingFl.add(ConversionUtils.convert(obj, Float.class));
                }
                // Create EmbedData.
                Integer index = datumDict.getInteger("index");
                builder.getData().add(new EmbedData(index, embeddingFl));
            }
            // Convert usage.
            Dict usageDict = Dict.of(BeanUtils.beanToMap(respDict.get("usage")));
            builder.setUsage(Usage.Builder.of()
                    .setPromptTokens(usageDict.getInteger("prompt_tokens"))
                    .setCompletionTokens(usageDict.getInteger("completion_tokens"))
                    .setTotalTokens(usageDict.getInteger("total_tokens"))
                    .build()
            );
            // Build.
            return builder.build();
        }

        public Proxy buildProxy(AbstractConfig config) {
            Assert.notNull(config, "Parameter \"config\" must not null. ");
            String  proxyType = config.getProxyType();
            String  proxyHostname = config.getProxyHostname();
            Integer proxyPort = config.getProxyPort();
            Assert.notBlank(proxyType, "Parameter \"proxyType\" must not blank. ");
            Assert.notBlank(proxyHostname, "Parameter \"proxyHostname\" must not blank. ");
            Assert.notNull(proxyPort, "Parameter \"proxyPort\" must not null. ");
            Proxy.Type type = Proxy.Type.valueOf(proxyType.toUpperCase());
            return new Proxy(type, new InetSocketAddress(proxyHostname, proxyPort));
        }

        public void checkResult(Dict result) {
            Object errorObj = result.get(ERROR_KEY);
            if (errorObj == null) { return; }
            Dict error = Dict.of(errorObj instanceof Map
                    ? (Map<?, ?>) errorObj : BeanUtils.beanToMap(errorObj));
            if (MapUtil.isEmpty(error)) { return; }
            String message = error.getString("message");
            String code = error.getString("code");
            Assert.state(StrUtil.isBlank(code) && StrUtil.isBlank(message), message);
        }
    }

}
