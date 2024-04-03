package baibao.ai.llm.support.ycm;

import artoria.ai.support.AbstractClassicAiHandler;
import artoria.common.constant.Symbols;
import artoria.data.Dict;
import artoria.data.bean.BeanUtils;
import artoria.data.json.JsonUtils;
import artoria.net.http.HttpMethod;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

import static artoria.common.constant.Numbers.ZERO;
import static java.lang.Boolean.FALSE;

/**
 * 基于鱼聪明AI接口封装的AI引擎.
 * @see <a href="https://www.yucongming.com/">鱼聪明 AI</>
 * @see <a href="https://github.com/liyupi/yucongming-java-sdk">鱼聪明 Java SDK</>
 * @author Kahle
 */
public abstract class AbstractYcmAiHandler extends AbstractClassicAiHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractYcmAiHandler.class);
    protected static final String API_URI = "https://www.yucongming.com/api/dev";
    protected static final String MODEL_ID_KEY = "modelId";
    protected static final String MESSAGE_KEY = "message";
    protected static final Long   DEFAULT_MODEL_ID = 1651468516836098050L;

    /**
     * Get the ycm ai engine configuration according to the arguments.
     * @param input The input arguments
     * @return The ycm ai engine configuration
     */
    protected abstract Config getConfig(Object input);

    protected String buildSign(Config config, String body) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + Symbols.DOT + config.getSecretKey();
        return md5.digestHex(content);
    }

    protected Map<String, String> getHeaders(Config config, String body) {
        String encodedBody = SecureUtil.md5(body);
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessKey", config.getAccessKey());
        headers.put("nonce", RandomUtil.randomNumbers(4));
        headers.put("body", encodedBody);
        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headers.put("sign", buildSign(config, encodedBody));
        return headers;
    }

    protected Object doHttp(HttpMethod method, String url, Object input, Config config) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        Assert.notBlank(url, "Parameter \"url\" must not blank. ");
        Boolean debug = config.getDebug();
        Proxy proxy = config.getProxy();

        String json = JsonUtils.toJsonString(input);
        Map<String, String> headers = getHeaders(config, json);
        if (debug != null && debug) {
            log.info("The yucongming api request \"{}\". \n(Headers: \"{}\")", json, headers);
        }
        HttpRequest request = HttpRequest.of(url)
                .method(Method.POST)
                .addHeaders(headers)
                .body(json);
        if (proxy != null) {
            request.setProxy(proxy);
        }
        String result = request.execute().body();
        if (debug != null && debug) {
            log.info("The yucongming api response \"{}\". ", result);
        }
        Assert.notBlank(result, "The yucongming api response data is blank. ");
        Dict respData = JsonUtils.parseObject(result, Dict.class);
        String  message = respData.getString("message");
        Integer code = respData.getInteger("code");
        Assert.state(ObjectUtils.equals(ZERO, code),
                String.format("The yucongming api invoke failure, the error message is \"%s\". ", message));
        return respData;
    }

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        if ("chat".equals(strategy)) { return chat(input, clazz); }
        else {
            throw new UnsupportedOperationException(
                "The method is unsupported. \n\n" +
                "The yucongming ai handler. \n" +
                "\n" +
                "Supported method:\n" +
                " - chat\n" +
                "     args: {message:str, modelId:long}"
            );
        }
    }

    public Object chat(Object input, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.isSupport(clazz, FALSE, Dict.class);
        Dict   inputData = Dict.of(BeanUtils.beanToMap(input));
        Config config = getConfig(inputData);
        String url = API_URI + "/chat";
        if (inputData.get(MODEL_ID_KEY) == null) {
            Long modelId = config.getModelId();
            inputData.set(MODEL_ID_KEY, modelId != null ? modelId : DEFAULT_MODEL_ID);
        }
        return doHttp(HttpMethod.POST, url, inputData, config);
    }

    public static class Config implements Serializable {
        private String accessKey;
        private String secretKey;
        private Long   modelId;
        private Proxy  proxy;
        private Boolean debug;

        public Config(String accessKey, String secretKey, Long modelId) {
            Assert.notBlank(accessKey, "Parameter \"accessKey\" must not blank. ");
            Assert.notBlank(secretKey, "Parameter \"secretKey\" must not blank. ");
            Assert.notNull(modelId, "Parameter \"modelId\" must not null. ");
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.modelId = modelId;
        }

        public Config(String accessKey, String secretKey) {
            Assert.notBlank(accessKey, "Parameter \"accessKey\" must not blank. ");
            Assert.notBlank(secretKey, "Parameter \"secretKey\" must not blank. ");
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        public Config() {

        }

        public String getAccessKey() {

            return accessKey;
        }

        public void setAccessKey(String accessKey) {

            this.accessKey = accessKey;
        }

        public String getSecretKey() {

            return secretKey;
        }

        public void setSecretKey(String secretKey) {

            this.secretKey = secretKey;
        }

        public Long getModelId() {

            return modelId;
        }

        public void setModelId(Long modelId) {

            this.modelId = modelId;
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
