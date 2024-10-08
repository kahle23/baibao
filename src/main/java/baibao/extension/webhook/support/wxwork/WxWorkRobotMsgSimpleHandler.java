/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.extension.webhook.support.wxwork;

import kunlun.action.support.AbstractStrategyActionHandler;
import kunlun.data.Dict;
import kunlun.data.json.JsonUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.net.http.HttpMethod;
import kunlun.net.http.HttpResponse;
import kunlun.net.http.HttpUtils;
import kunlun.net.http.support.SimpleRequest;
import kunlun.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Boolean.FALSE;
import static kunlun.common.constant.Charsets.STR_UTF_8;

/**
 * Work WeChat message robot.
 * @author Kahle
 */
public class WxWorkRobotMsgSimpleHandler extends AbstractStrategyActionHandler {
    private static final Logger log = LoggerFactory.getLogger(WxWorkRobotMsgSimpleHandler.class);
    private final String url;

    public WxWorkRobotMsgSimpleHandler(String key) {
        Assert.notBlank(key, "Parameter \"key\" must not blank. ");
        this.url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key="+key;
    }

    public Object send(Object message) {
        try {
            SimpleRequest request = new SimpleRequest();
            request.setUrl(url);
            request.setMethod(HttpMethod.POST);
            request.setCharset(STR_UTF_8);
            request.addHeader("Content-Type", "application/json");

            Dict dict = Dict.of("msgtype", "text").set("text", Dict.of("content", message));

            request.setBody(JsonUtils.toJsonString(dict));
            log.info("WxWorkMessageRobot send \"{}\". ", JsonUtils.toJsonString(request));
            HttpResponse httpResponse = HttpUtils.execute(request);
            String bodyAsString = httpResponse.getBodyAsString();
            log.info("WxWorkMessageRobot receive \"{}\". ", bodyAsString);
            return bodyAsString;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.isSupport(clazz, FALSE, String.class, Object.class);
        return send(input);
    }

}