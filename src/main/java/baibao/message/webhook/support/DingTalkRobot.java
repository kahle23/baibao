package baibao.message.webhook.support;

import artoria.codec.CodecUtils;
import artoria.crypto.Hmac;
import artoria.crypto.KeyUtils;
import artoria.data.json.JsonUtils;
import artoria.exception.ExceptionUtils;
import artoria.message.handler.AbstractClassicMessageHandler;
import artoria.net.http.HttpClient;
import artoria.net.http.HttpMethod;
import artoria.net.http.HttpResponse;
import artoria.net.http.HttpUtils;
import artoria.net.http.support.SimpleRequest;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;

/**
 * The ding talk robot.
 * @author Kahle
 */
@Deprecated
public class DingTalkRobot extends AbstractClassicMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(DingTalkRobot.class);
    private final HttpClient httpClient;
    private final String webHook;
    private final String secret;

    public DingTalkRobot(String webHook, String secret) {

        this(HttpUtils.getHttpClient(HttpUtils.getDefaultClientName()), webHook, secret);
    }

    public DingTalkRobot(HttpClient httpClient, String webHook, String secret) {
        Assert.notNull(httpClient, "Parameter \"httpClient\" must not null. ");
        Assert.notBlank(webHook, "Parameter \"webHook\" must not blank. ");
        Assert.notBlank(secret, "Parameter \"secret\" must not blank. ");
        this.httpClient = httpClient;
        this.webHook = webHook;
        this.secret = secret;
    }

    protected String sign(Long timestamp, String secret) {
        try {
            String strToSign = timestamp + "\n" + secret;
            byte[] bytesToSign = strToSign.getBytes(UTF_8);
            byte[] secretBytes = secret.getBytes(UTF_8);
            SecretKey secretKey =
                    KeyUtils.parseSecretKey(HMAC_SHA256, secretBytes);
            Hmac hmac = new Hmac(HMAC_SHA256);
            hmac.setSecretKey(secretKey);
            byte[] digest = hmac.digest(bytesToSign);
            String base64Str = CodecUtils.encodeToString(CodecUtils.BASE64, digest);
            return URLEncoder.encode(base64Str, UTF_8);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    protected void at(Map<String, Object> data, boolean atAll, List<String> atList) {
        if (!atAll && CollectionUtils.isEmpty(atList)) { return; }
        Map<String, Object> atMap = new HashMap<String, Object>(FOUR);
        if (CollectionUtils.isNotEmpty(atList)) {
            atMap.put("atMobiles", atList);
        }
        atMap.put("isAtAll", atAll);
        data.put("at", atMap);
    }

    public Object sendMarkdown(String title, String text, boolean atAll, List<String> atList) {
        Map<String, Object> data = new HashMap<String, Object>(FOUR);
        data.put("msgtype", "markdown");
        Map<String, Object> markdownMap = new HashMap<String, Object>(FOUR);
        markdownMap.put("title", title);
        markdownMap.put("text", text);
        data.put("markdown", markdownMap);
        at(data, atAll, atList);
        return send(JsonUtils.toJsonString(data));
    }

    public Object sendText(String content, boolean atAll, List<String> atList) {
        Map<String, Object> data = new HashMap<String, Object>(FOUR);
        data.put("msgtype", "text");
        Map<String, Object> textMap = new HashMap<String, Object>(TWO);
        textMap.put("content", content);
        data.put("text", textMap);
        at(data, atAll, atList);
        return send(JsonUtils.toJsonString(data));
    }

    public Object send(Object message) {
        try {
            Long timestamp = System.currentTimeMillis();
            String sign = sign(timestamp, secret);
            String fullUrl = webHook;
            fullUrl += "&timestamp=" + timestamp + "&sign=" + sign;
            SimpleRequest request = new SimpleRequest();
            request.setUrl(fullUrl);
            request.setMethod(HttpMethod.POST);
            request.setCharset(UTF_8);
            request.addHeader("Content-Type", "application/json");
            if (message instanceof String) {
                String str = String.valueOf(message);
                boolean normal = StringUtils.isNotBlank(str)
                        && str.startsWith("{") && str.endsWith("}");
                if (normal) {
                    log.info("DingTalk robot send \"{}\". ", message);
                    request.setBody(message);
                }
                else {
                    return sendText(str, false, null);
                }
            }
            else {
                log.info("DingTalk robot send \"{}\". ", JsonUtils.toJsonString(request));
                request.setBody(JsonUtils.toJsonString(message));
            }
            HttpResponse httpResponse = httpClient.execute(request);
            String bodyAsString = httpResponse.getBodyAsString();
            log.info("DingTalk robot receive \"{}\". ", bodyAsString);
            return bodyAsString;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object operate(Object input, String name, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        if ("send".equals(name)) {
            isSupport(new Class[]{ String.class }, clazz);
            return send(input);
        }
        else {
            throw new UnsupportedOperationException(
                    "Unsupported operation name \"" + name + "\"! "
            );
        }
    }

}
