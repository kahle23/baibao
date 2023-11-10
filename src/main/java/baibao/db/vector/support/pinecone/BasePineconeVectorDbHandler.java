package baibao.db.vector.support.pinecone;

import artoria.data.Dict;
import artoria.data.bean.BeanUtils;
import artoria.data.json.JsonUtils;
import artoria.db.AbstractDbHandler;
import artoria.db.vector.VectorDbHandler;
import artoria.net.http.HttpMethod;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;

import static artoria.common.constant.Numbers.*;

/**
 * The base pinecone vector database handler.
 * <a href="https://www.pinecone.io/">Pinecone</a>
 * <a href="https://docs.pinecone.io/reference">Pinecone API Reference</a>
 * @author Kahle
 */
public abstract class BasePineconeVectorDbHandler extends AbstractDbHandler implements VectorDbHandler {
    private static final Logger log = LoggerFactory.getLogger(BasePineconeVectorDbHandler.class);

    /**
     * Get the database configuration according to the arguments.
     * @param arguments The input arguments
     * @return The database configuration
     */
    protected abstract Config getConfig(Object arguments);

    protected Object doHttp(HttpMethod method, String url, Object input, Config config) {
        Boolean debug = config.getDebug();
        Proxy proxy = config.getProxy();

        Method mtd;
        if (HttpMethod.POST.equals(method)) {
            mtd = Method.POST;
        }
        else if (HttpMethod.GET.equals(method)) {
            mtd = Method.GET;
        }
        else {
            throw new UnsupportedOperationException("The http method is unsupported. ");
        }

        HttpRequest request = HttpUtil.createRequest(mtd, url);
        request.header("content-type", "application/json");
        request.header("Api-Key", config.getApiKey());

        if (HttpMethod.POST.equals(method)) {
            request.body(JsonUtils.toJsonString(input));
        }
        else {
            request.form(BeanUtils.beanToMap(input));
        }


        if (proxy != null) {
            request.setProxy(proxy);
        }

        if (debug != null && debug) {
            log.info("Http pinecone input: {}", JsonUtils.toJsonString(input));
        }
        HttpResponse response = request.execute();
        String responseBody = response.body();
        if (debug != null && debug) {
            log.info("Http pinecone output: {}", responseBody);
        }

        if (StrUtil.isBlank(responseBody)) { return null; }
        Dict respData = JsonUtils.parseObject(responseBody, Dict.class);

        String message = respData.getString("message");
        Integer code = respData.getInteger("code");
        if (code != null) {
            throw new IllegalStateException(String.format("%s (code: %s)", message, code));
        }
        return respData;
    }

    @Override
    public Object execute(Object[] arguments) {
        String methodName = String.valueOf(arguments[ZERO]);
        Object input = arguments[ONE];
        Object type = arguments[TWO];
        if ("describeIndexStats".equals(methodName)) { return describeIndexStats(input); }
        if ("query".equals(methodName)) { return query(input); }
        if ("delete".equals(methodName)) { return delete(input); }
        if ("fetch".equals(methodName)) { return fetch(input); }
        if ("update".equals(methodName)) { return update(input); }
        if ("upsert".equals(methodName)) { return upsert(input); }
        throw new UnsupportedOperationException();
    }

    public Object describeIndexStats(Object condition) {
        Config config = getConfig(condition);
        String url = config.getHost() + "/describe_index_stats";
        return doHttp(HttpMethod.POST, url, condition, config);
    }

    public Object query(Object condition) {
        Config config = getConfig(condition);
        String url = config.getHost() + "/query";
        return doHttp(HttpMethod.POST, url, condition, config);
    }

    public Object delete(Object condition) {
        Config config = getConfig(condition);
        String url = config.getHost() + "/vectors/delete";
        return doHttp(HttpMethod.POST, url, condition, config);
    }

    public Object fetch(Object condition) {
        Config config = getConfig(condition);
        String url = config.getHost() + "/vectors/fetch";
        return doHttp(HttpMethod.GET, url, BeanUtils.beanToMap(condition), config);
    }

    public Object update(Object data) {
        Config config = getConfig(data);
        String url = config.getHost() + "/vectors/update";
        return doHttp(HttpMethod.POST, url, data, config);
    }

    public Object upsert(Object data) {
        Config config = getConfig(data);
        String url = config.getHost() + "/vectors/upsert";
        return doHttp(HttpMethod.POST, url, data, config);
    }

    /**
     * The pinecone database configuration.
     * @author Kahle
     */
    public static class Config {
        private String host;
        private String apiKey;
        private Proxy  proxy;
        private Boolean debug;

        public Config(String host, String apiKey, Proxy proxy) {
            this.apiKey = apiKey;
            this.proxy = proxy;
            this.host = host;
        }

        public Config(String host, String apiKey) {
            this.apiKey = apiKey;
            this.host = host;
        }

        public Config() {

        }

        public String getHost() {

            return host;
        }

        public void setHost(String host) {

            this.host = host;
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
