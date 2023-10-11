package baibao.message.webhook.support;

import artoria.bot.MessageBot;
import artoria.codec.CodecUtils;
import artoria.crypto.Hmac;
import artoria.crypto.KeyUtils;
import artoria.exception.ExceptionUtils;
import artoria.exchange.JsonUtils;
import artoria.net.*;
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
public class DingTalkRobot implements MessageBot {
    private static final Logger log = LoggerFactory.getLogger(DingTalkRobot.class);
    private final HttpClient httpClient;
    private final String webHook;
    private final String secret;

    public DingTalkRobot(String webHook, String secret) {

        this(HttpUtils.getHttpClient(), webHook, secret);
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

    @Override
    public Object send(Object message) {
        try {
            Long timestamp = System.currentTimeMillis();
            String sign = sign(timestamp, secret);
            String fullUrl = webHook;
            fullUrl += "&timestamp=" + timestamp + "&sign=" + sign;
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setUrl(fullUrl);
            httpRequest.setMethod(HttpMethod.POST);
            httpRequest.setCharset(UTF_8);
            httpRequest.addHeader("Content-Type", "application/json");
            if (message instanceof String) {
                String str = String.valueOf(message);
                boolean normal = StringUtils.isNotBlank(str)
                        && str.startsWith("{") && str.endsWith("}");
                if (normal) {
                    log.info("DingTalk robot send \"{}\". ", message);
                    httpRequest.setBody(message);
                }
                else {
                    return sendText(str, false, null);
                }
            }
            else {
                log.info("DingTalk robot send \"{}\". ", JsonUtils.toJsonString(httpRequest));
                httpRequest.setBody(JsonUtils.toJsonString(message));
            }
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            String bodyAsString = httpResponse.getBodyAsString();
            log.info("DingTalk robot receive \"{}\". ", bodyAsString);
            return bodyAsString;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
