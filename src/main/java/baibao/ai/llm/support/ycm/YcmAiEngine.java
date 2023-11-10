package baibao.ai.llm.support.ycm;

import artoria.ai.AbstractClassicAiEngine;
import artoria.ai.llm.LLM;
import artoria.ai.llm.support.AiMessage;
import artoria.common.constant.Symbols;
import artoria.data.Dict;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

import static artoria.common.constant.Numbers.ZERO;
import static java.lang.Boolean.FALSE;

/**
 * 基于鱼聪明AI接口封装的AI引擎.
 * @see <a href="https://github.com/liyupi/yucongming-java-sdk">鱼聪明 Java SDK</>
 * @author Kahle
 */
public class YcmAiEngine extends AbstractClassicAiEngine implements LLM {
    private static final Logger log = LoggerFactory.getLogger(YcmAiEngine.class);
    private static final String API_URI = "https://www.yucongming.com/api/dev";
    private static final String MODEL_ID_KEY = "modelId";
    private static final String MESSAGE_KEY = "message";
    private final String accessKey;
    private final String secretKey;
    private final Long defModelId;
    private Proxy proxy;

    public YcmAiEngine(Long defModelId, String accessKey, String secretKey) {
        Assert.notBlank(accessKey, "Parameter \"accessKey\" must not blank. ");
        Assert.notBlank(secretKey, "Parameter \"secretKey\" must not blank. ");
        Assert.notNull(defModelId, "Parameter \"defModelId\" must not null. ");
        this.defModelId = defModelId;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public YcmAiEngine(String accessKey, String secretKey) {

        this(1651468516836098050L, accessKey, secretKey);
    }

    public Proxy getProxy() {

        return proxy;
    }

    public void setProxy(Proxy proxy) {

        this.proxy = proxy;
    }

    protected String buildSign(String body, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + Symbols.DOT + secretKey;
        return md5.digestHex(content);
    }

    protected Map<String, String> getHeaders(String body) {
        String encodedBody = SecureUtil.md5(body);
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("accessKey", accessKey);
        headers.put("nonce", RandomUtil.randomNumbers(4));
        headers.put("body", encodedBody);
        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headers.put("sign", buildSign(encodedBody, secretKey));
        return headers;
    }

    protected String getUrl(String strategy) {

        return API_URI + "/chat";
    }

    protected Dict convertInput(Object input, String strategy) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Dict dict;
        if (input instanceof String) {
            dict = Dict.of(MESSAGE_KEY, input);
        }
        else if (input instanceof Dict) {
            dict = (Dict) input;
        }
        else if (input instanceof Map) {
            dict = Dict.of((Map<?, ?>) input);
        }
        else if (input instanceof AiMessage) {
            AiMessage msg = (AiMessage) input;
            String content = msg.getContent();
//            String model = msg.getModel();
            dict = Dict.of(MESSAGE_KEY, content);
//            if (StrUtil.isNotBlank(model)) {
//                dict.set(MODEL_ID_KEY, model);
//            }
        }
        else {
            throw new UnsupportedOperationException("Parameter \"input\" is unsupported! ");
        }
        if (dict.get(MODEL_ID_KEY) == null) {
            dict.set(MODEL_ID_KEY, defModelId);
        }
        Assert.notBlank(dict.getString(MESSAGE_KEY)
                , "Parameter \"message\" must not blank. ");
        return dict;
    }

    protected Object convertOutput(String responseData, String strategy, Class<?> clazz) {
        Dict dataDict = JSONUtil.toBean(responseData, Dict.class);
        return dataDict != null ? dataDict.getString("content") : null;
    }

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        Assert.isSupport(clazz, FALSE, String.class, CharSequence.class);
        String json = JSONUtil.toJsonStr(convertInput(input, strategy));
        Map<String, String> headers = getHeaders(json);
        log.debug("The yucongming api request \"{}\". \n(Headers: \"{}\")", json, headers);
        HttpRequest request = HttpRequest.post(getUrl(strategy))
                .addHeaders(headers)
                .body(json);
        if (getProxy() != null) {
            request.setProxy(getProxy());
        }
        String result = request.execute().body();
        log.debug("The yucongming api response \"{}\". ", result);
        Assert.notBlank(result, "The yucongming api response data is blank. ");
        Dict response = JSONUtil.toBean(result, Dict.class);
        String  message = response.getString("message");
        Integer code = response.getInteger("code");
        Assert.state(ObjectUtils.equals(ZERO, code)
                , String.format("The yucongming api invoke failure, the error message is \"%s\". ", message));
        return convertOutput(response.getString("data"), strategy, clazz);
    }

}
